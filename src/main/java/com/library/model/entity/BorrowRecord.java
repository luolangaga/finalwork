package com.library.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "borrow_record")
public class BorrowRecord {

    @Id
    @Column(name = "record_id", nullable = false)
    private String recordId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_id")
    private Borrower borrower;

    @Column(name = "resource_id", nullable = false)
    private String resourceId;

    @Column(name = "borrow_date", nullable = false)
    private LocalDate borrowDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "return_date")
    private LocalDate returnDate;

    private boolean returned = false;

    protected BorrowRecord() {}

    public BorrowRecord(String recordId, String borrowerId,
                        String resourceId, LocalDate borrowDate,
                        LocalDate dueDate) {
        this.recordId = recordId;
        this.resourceId = resourceId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returned = false;
    }

    public void doReturn() {
        this.returnDate = LocalDate.now();
        this.returned = true;
    }

    public boolean isOverdue() {
        return !returned && LocalDate.now().isAfter(dueDate);
    }

    public long getOverdueDays() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    public String getRecordId() { return recordId; }
    public void setRecordId(String recordId) { this.recordId = recordId; }
    public Borrower getBorrower() { return borrower; }
    public void setBorrower(Borrower borrower) { this.borrower = borrower; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }
    public boolean isReturned() { return returned; }
    public void setReturned(boolean returned) { this.returned = returned; }
}
