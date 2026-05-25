package com.library.model.policy;

public interface BorrowPolicy {
    int getMaxBorrowCount();
    int getMaxBorrowDays(String resourceType);
    boolean canRenew();
    int getMaxRenewTimes();
}
