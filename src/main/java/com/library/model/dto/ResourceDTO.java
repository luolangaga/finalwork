package com.library.model.dto;

import java.util.Map;

public class ResourceDTO {

    private String id;
    private String title;
    private String type;
    private String status;
    private String borrowerId;
    private String borrowDate;
    private String dueDate;
    private Integer borrowDays;
    private Map<String, Object> extraAttrs;

    public ResourceDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getBorrowerId() { return borrowerId; }
    public void setBorrowerId(String borrowerId) { this.borrowerId = borrowerId; }
    public String getBorrowDate() { return borrowDate; }
    public void setBorrowDate(String borrowDate) { this.borrowDate = borrowDate; }
    public String getDueDate() { return dueDate; }
    public void setDueDate(String dueDate) { this.dueDate = dueDate; }
    public Integer getBorrowDays() { return borrowDays; }
    public void setBorrowDays(Integer borrowDays) { this.borrowDays = borrowDays; }
    public Map<String, Object> getExtraAttrs() { return extraAttrs; }
    public void setExtraAttrs(Map<String, Object> extraAttrs) { this.extraAttrs = extraAttrs; }
}