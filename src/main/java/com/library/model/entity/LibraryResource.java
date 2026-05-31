package com.library.model.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "resource_type", discriminatorType = DiscriminatorType.STRING)
public abstract class LibraryResource {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    private ResourceStatus status = ResourceStatus.AVAILABLE;

    private String borrowerId;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    public enum ResourceStatus {
        AVAILABLE, BORROWED, RESERVED
    }

    protected LibraryResource() {}

    public LibraryResource(String id, String title) {
        this.id = id;
        this.title = title;
        this.status = ResourceStatus.AVAILABLE;
    }

    public abstract int getMaxBorrowDays();

    public abstract String getResourceType();

    @Transient
    public boolean isAvailable() {
        return status == ResourceStatus.AVAILABLE;
    }

    public boolean borrow(String borrowerId) {
        if (status != ResourceStatus.AVAILABLE) {
            return false;
        }
        this.borrowerId = borrowerId;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(getMaxBorrowDays());
        this.status = ResourceStatus.BORROWED;
        return true;
    }

    public boolean returnResource() {
        if (status != ResourceStatus.BORROWED) {
            return false;
        }
        this.borrowerId = null;
        this.borrowDate = null;
        this.dueDate = null;
        this.status = ResourceStatus.AVAILABLE;
        return true;
    }

    public boolean isOverdue() {
        return status == ResourceStatus.BORROWED
                && LocalDate.now().isAfter(dueDate);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public ResourceStatus getStatus() { return status; }
    public void setStatus(ResourceStatus status) { this.status = status; }
    public String getBorrowerId() { return borrowerId; }
    public void setBorrowerId(String borrowerId) { this.borrowerId = borrowerId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
}
