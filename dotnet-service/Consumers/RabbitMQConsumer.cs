using System.Text;
using System.Text.Json;
using DotnetService.Data;
using DotnetService.Models;
using DotnetService.Services;
using Microsoft.EntityFrameworkCore;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;

namespace DotnetService.Consumers;

public class RabbitMQConsumer : IDisposable
{
    private readonly IServiceScopeFactory _scopeFactory;
    private IConnection? _connection;
    private IChannel? _channel;
    private readonly string _host;
    private readonly int _port;
    private readonly string _user;
    private readonly string _pass;

    public RabbitMQConsumer(IServiceScopeFactory scopeFactory, IConfiguration config)
    {
        _scopeFactory = scopeFactory;
        var rabbit = config.GetSection("RabbitMQ");
        _host = rabbit["HostName"] ?? "localhost";
        _port = int.Parse(rabbit["Port"] ?? "5672");
        _user = rabbit["UserName"] ?? "guest";
        _pass = rabbit["Password"] ?? "guest";
    }

    public async Task StartConsuming()
    {
        var factory = new ConnectionFactory
        {
            HostName = _host,
            Port = _port,
            UserName = _user,
            Password = _pass,
            AutomaticRecoveryEnabled = true
        };
        _connection = await factory.CreateConnectionAsync();
        _channel = await _connection.CreateChannelAsync();

        await _channel.ExchangeDeclareAsync("library.event", ExchangeType.Topic, durable: true);
        await _channel.QueueDeclareAsync("analytics.borrow", durable: true, exclusive: false, autoDelete: false);
        await _channel.QueueDeclareAsync("analytics.notification", durable: true, exclusive: false, autoDelete: false);
        await _channel.QueueBindAsync("analytics.borrow", "library.event", "borrow.#");
        await _channel.QueueBindAsync("analytics.notification", "library.event", "notification.#");

        var consumer = new AsyncEventingBasicConsumer(_channel);
        consumer.ReceivedAsync += async (_, ea) =>
        {
            using var scope = _scopeFactory.CreateScope();
            var db = scope.ServiceProvider.GetRequiredService<AnalyticsDbContext>();
            var notifSvc = scope.ServiceProvider.GetRequiredService<NotificationService>();
            var body = Encoding.UTF8.GetString(ea.Body.ToArray());
            await ProcessMessage(body, ea.RoutingKey, db, notifSvc);
            await _channel!.BasicAckAsync(ea.DeliveryTag, false);
        };

        await _channel.BasicConsumeAsync("analytics.borrow", false, consumer);
        await _channel.BasicConsumeAsync("analytics.notification", false, consumer);
    }

    private static async Task ProcessMessage(string body, string routingKey,
        AnalyticsDbContext db, NotificationService notifSvc)
    {
        using var doc = JsonDocument.Parse(body);
        var root = doc.RootElement;

        db.BorrowEventLogs.Add(new BorrowEventLog
        {
            EventType = routingKey.Contains("borrow.created") ? "borrow" : "return",
            ResourceId = root.TryGetProperty("resourceId", out var rid) ? rid.GetString() ?? "" : "",
            BorrowerId = root.TryGetProperty("borrowerId", out var bid) ? bid.GetString() ?? "" : "",
            EventTime = DateTime.UtcNow,
            Payload = body
        });
        await db.SaveChangesAsync();

        if (routingKey.Contains("borrow.created"))
        {
            var borrowerId = root.TryGetProperty("borrowerId", out var b) ? b.GetString() ?? "" : "";
            var dueDate = root.TryGetProperty("dueDate", out var d) ? d.GetString() ?? "" : "";
            await notifSvc.CreateNotification(borrowerId, "borrow",
                $"借阅成功，到期日: {dueDate}");
        }
    }

    public void Dispose()
    {
        _channel?.Dispose();
        _connection?.Dispose();
    }
}
