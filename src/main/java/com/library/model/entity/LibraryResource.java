package com.library.model.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import org.hibernate.annotations.Type;
import java.time.LocalDate;
import java.util.Map;

@Entity
@Table(name = "library_resource")
public abstract class LibraryResource {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(name = "resource_type", nullable = false, length = 31)
    private String type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceStatus status = ResourceStatus.AVAILABLE;

    private String borrowerId;
    private LocalDate borrowDate;
    private LocalDate dueDate;

    @Type(JsonType.class)
    @Column(name = "extra_attrs", columnDefinition = "jsonb")
    private Map<String, Object> extraAttrs;

    public enum ResourceStatus {
        AVAILABLE, BORROWED, RESERVED
    }

    protected LibraryResource() {}

    public LibraryResource(String id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
        this.status = ResourceStatus.AVAILABLE;
    }

    public abstract int getMaxBorrowDays();

    public final boolean borrow(String borrowerId) {
        if (status != ResourceStatus.AVAILABLE) {
            return false;
        }
        this.borrowerId = borrowerId;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(getMaxBorrowDays());
        this.status = ResourceStatus.BORROWED;
        return true;
    }

    public final boolean returnResource() {
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
        return status == ResourceStatus.BORROWED && LocalDate.now().isAfter(dueDate);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public ResourceStatus getStatus() { return status; }
    public void setStatus(ResourceStatus status) { this.status = status; }
    public String getBorrowerId() { return borrowerId; }
    public void setBorrowerId(String borrowerId) { this.borrowerId = borrowerId; }
    public LocalDate getBorrowDate() { return borrowDate; }
    public void setBorrowDate(LocalDate borrowDate) { this.borrowDate = borrowDate; }
    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
    public Map<String, Object> getExtraAttrs() { return extraAttrs; }
    public void setExtraAttrs(Map<String, Object> extraAttrs) { this.extraAttrs = extraAttrs; }
}
