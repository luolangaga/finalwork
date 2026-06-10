using DotnetService.Data;
using Microsoft.EntityFrameworkCore;

namespace DotnetService.Services;

public class ReportService
{
    private readonly AnalyticsDbContext _db;
    public ReportService(AnalyticsDbContext db) => _db = db;

    public async Task<object> GenerateReport(string type)
    {
        return type switch
        {
            "borrow" => await GenerateBorrowReport(),
            "overdue" => await GenerateOverdueReport(),
            _ => new { error = "Unknown report type" }
        };
    }

    private async Task<object> GenerateBorrowReport()
    {
        var lastMonth = DateTime.UtcNow.AddMonths(-1);
        var total = await _db.BorrowEventLogs
            .CountAsync(e => e.EventType == "borrow" && e.EventTime >= lastMonth);
        var returns = await _db.BorrowEventLogs
            .CountAsync(e => e.EventType == "return" && e.EventTime >= lastMonth);

        return new
        {
            reportType = "borrow",
            period = "monthly",
            generatedAt = DateTime.UtcNow,
            totalBorrows = total,
            totalReturns = returns,
            activeBorrows = total - returns
        };
    }

    private async Task<object> GenerateOverdueReport()
    {
        var overdueNotifications = await _db.Notifications
            .Where(n => n.Type == "overdue" && !n.IsRead)
            .ToListAsync();

        return new
        {
            reportType = "overdue",
            generatedAt = DateTime.UtcNow,
            overdueCount = overdueNotifications.Count,
            details = overdueNotifications.Select(n => new
            {
                n.UserId,
                n.Content,
                n.CreatedAt
            })
        };
    }
}
