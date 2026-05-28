package com.library.model.dto;

public class BorrowRequest {

    private String borrowerId;
    private String resourceId;

    public BorrowRequest() {}

    public BorrowRequest(String borrowerId, String resourceId) {
        this.borrowerId = borrowerId;
        this.resourceId = resourceId;
    }

    public String getBorrowerId() { return borrowerId; }
    public void setBorrowerId(String borrowerId) { this.borrowerId = borrowerId; }
    public String getResourceId() { return resourceId; }
    public void setResourceId(String resourceId) { this.resourceId = resourceId; }
}
