using System.Text.Json;
using DotnetService.Data;
using DotnetService.Services;
using DotnetService.Consumers;
using Microsoft.EntityFrameworkCore;

var builder = WebApplication.CreateBuilder(args);

builder.Services.AddDbContext<AnalyticsDbContext>(options =>
    options.UseNpgsql(
        builder.Configuration.GetConnectionString("PostgreSQL")));

builder.Services.AddScoped<StatisticsService>();
builder.Services.AddScoped<ReportService>();
builder.Services.AddScoped<NotificationService>();
builder.Services.AddSingleton<RabbitMQConsumer>();
builder.Services.AddCors(o => o.AddDefaultPolicy(p =>
    p.AllowAnyOrigin().AllowAnyMethod().AllowAnyHeader()));

var app = builder.Build();
app.UseCors();

app.Use(async (context, next) =>
{
    try
    {
        await next();
    }
    catch (Exception ex)
    {
        context.Response.StatusCode = 500;
        context.Response.ContentType = "application/json";
        await context.Response.WriteAsync(JsonSerializer.Serialize(
            new { error = ex.Message }));
    }
});

app.MapGet("/api/statistics/trends", async (StatisticsService svc) =>
    Results.Ok(await svc.GetBorrowTrends()));

app.MapGet("/api/statistics/hot-resources", async (StatisticsService svc) =>
    Results.Ok(await svc.GetHotResources()));

app.MapGet("/api/statistics/type-distribution", async (StatisticsService svc) =>
    Results.Ok(await svc.GetTypeDistribution()));

app.MapPost("/api/reports/generate", async (ReportService svc, HttpRequest req) =>
{
    var body = await JsonSerializer.DeserializeAsync<Dictionary<string, JsonElement>>(req.Body);
    var type = body?.GetValueOrDefault("type", default).GetString() ?? "borrow";
    var result = await svc.GenerateReport(type);
    return Results.Ok(result);
});

app.MapGet("/api/notifications/{userId}", async (string userId, NotificationService svc) =>
    Results.Ok(await svc.GetNotifications(userId)));

app.MapPost("/api/notifications/mark-read", async (NotificationService svc, HttpRequest req) =>
{
    var body = await JsonSerializer.DeserializeAsync<Dictionary<string, JsonElement>>(req.Body);
    var notifId = body?.GetValueOrDefault("notificationId", default).GetString() ?? "";
    await svc.MarkAsRead(notifId);
    return Results.Ok(new { success = true });
});

using (var scope = app.Services.CreateScope())
{
    var db = scope.ServiceProvider.GetRequiredService<AnalyticsDbContext>();
    await db.Database.EnsureCreatedAsync();
}

var consumer = app.Services.GetRequiredService<RabbitMQConsumer>();
_ = Task.Run(() => consumer.StartConsuming());

app.Run();
