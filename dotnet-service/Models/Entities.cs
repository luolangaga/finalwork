namespace DotnetService.Models;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;

[Table("notification", Schema = "analytics")]
public class Notification
{
    [Key] public string Id { get; set; } = Guid.NewGuid().ToString();
    public string UserId { get; set; } = "";
    public string Type { get; set; } = "";
    public string Content { get; set; } = "";
    public bool IsRead { get; set; } = false;
    public DateTime CreatedAt { get; set; } = DateTime.UtcNow;
}

[Table("statistics_snapshot", Schema = "analytics")]
public class StatisticsSnapshot
{
    [Key] public string Id { get; set; } = Guid.NewGuid().ToString();
    public DateTime Date { get; set; }
    public string ResourceType { get; set; } = "";
    public int BorrowCount { get; set; }
    public int ReturnCount { get; set; }
}

[Table("borrow_event_log", Schema = "analytics")]
public class BorrowEventLog
{
    [Key] public string Id { get; set; } = Guid.NewGuid().ToString();
    public string ResourceId { get; set; } = "";
    public string BorrowerId { get; set; } = "";
    public string EventType { get; set; } = "";
    public DateTime EventTime { get; set; } = DateTime.UtcNow;
    public string? Payload { get; set; }
}
