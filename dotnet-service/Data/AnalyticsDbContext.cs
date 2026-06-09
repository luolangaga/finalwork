using DotnetService.Models;
using Microsoft.EntityFrameworkCore;

namespace DotnetService.Data;

public class AnalyticsDbContext : DbContext
{
    public AnalyticsDbContext(DbContextOptions<AnalyticsDbContext> options) : base(options) {}

    public DbSet<Notification> Notifications { get; set; }
    public DbSet<StatisticsSnapshot> StatisticsSnapshots { get; set; }
    public DbSet<BorrowEventLog> BorrowEventLogs { get; set; }

    protected override void OnModelCreating(ModelBuilder modelBuilder)
    {
        modelBuilder.HasDefaultSchema("analytics");
        modelBuilder.Entity<Notification>().HasIndex(n => n.UserId);
        modelBuilder.Entity<BorrowEventLog>().HasIndex(e => e.ResourceId);
        modelBuilder.Entity<BorrowEventLog>().HasIndex(e => e.EventTime);
    }
}
