package com.library.model.policy;

public class StudentBorrowPolicy implements BorrowPolicy {

    @Override
    public int getMaxBorrowCount() {
        return 5;
    }

    @Override
    public int getMaxBorrowDays(String resourceType) {
        return switch (resourceType) {
            case "BOOK" -> 30;
            case "MAGAZINE" -> 14;
            case "DVD" -> 7;
            case "EBOOK" -> 21;
            default -> 14;
        };
    }

    @Override
    public boolean canRenew() {
        return true;
    }

    @Override
    public int getMaxRenewTimes() {
        return 1;
    }
}
