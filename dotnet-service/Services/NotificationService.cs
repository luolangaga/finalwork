using DotnetService.Data;
using DotnetService.Models;
using Microsoft.EntityFrameworkCore;

namespace DotnetService.Services;

public class NotificationService
{
    private readonly AnalyticsDbContext _db;
    public NotificationService(AnalyticsDbContext db) => _db = db;

    public async Task<List<Notification>> GetNotifications(string userId) =>
        await _db.Notifications
            .Where(n => n.UserId == userId)
            .OrderByDescending(n => n.CreatedAt)
            .Take(50)
            .ToListAsync();

    public async Task MarkAsRead(string notificationId)
    {
        var notif = await _db.Notifications.FindAsync(notificationId);
        if (notif != null)
        {
            notif.IsRead = true;
            await _db.SaveChangesAsync();
        }
    }

    public async Task CreateNotification(string userId, string type, string content)
    {
        _db.Notifications.Add(new Notification
        {
            UserId = userId,
            Type = type,
            Content = content,
            CreatedAt = DateTime.UtcNow
        });
        await _db.SaveChangesAsync();
    }

    public async Task<int> GetUnreadCount(string userId) =>
        await _db.Notifications.CountAsync(n => n.UserId == userId && !n.IsRead);
}
