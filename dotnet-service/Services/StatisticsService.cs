using DotnetService.Data;
using DotnetService.Models;
using Microsoft.EntityFrameworkCore;

namespace DotnetService.Services;

public class StatisticsService
{
    private readonly AnalyticsDbContext _db;
    public StatisticsService(AnalyticsDbContext db) => _db = db;

    public async Task<object> GetBorrowTrends()
    {
        var last30Days = DateTime.UtcNow.AddDays(-30);
        var events = await _db.BorrowEventLogs
            .Where(e => e.EventTime >= last30Days)
            .GroupBy(e => e.EventTime.Date)
            .Select(g => new
            {
                Date = g.Key.ToString("yyyy-MM-dd"),
                Borrow = g.Count(e => e.EventType == "borrow"),
                Return = g.Count(e => e.EventType == "return")
            })
            .OrderBy(x => x.Date)
            .ToListAsync();

        return new { period = "30d", trends = events };
    }

    public async Task<object> GetHotResources()
    {
        var hot = await _db.BorrowEventLogs
            .Where(e => e.EventType == "borrow")
            .GroupBy(e => e.ResourceId)
            .Select(g => new { resourceId = g.Key, count = g.Count() })
            .OrderByDescending(x => x.count)
            .Take(10)
            .ToListAsync();

        return new { top = 10, resources = hot };
    }

    public async Task<object> GetTypeDistribution()
    {
        var today = DateTime.UtcNow.Date;
        var snapshots = await _db.StatisticsSnapshots
            .Where(s => s.Date == today)
            .GroupBy(s => s.ResourceType)
            .Select(g => new
            {
                type = g.Key,
                borrowCount = g.Sum(s => s.BorrowCount),
                returnCount = g.Sum(s => s.ReturnCount)
            })
            .ToListAsync();

        return new { date = today.ToString("yyyy-MM-dd"), distribution = snapshots };
    }
}
