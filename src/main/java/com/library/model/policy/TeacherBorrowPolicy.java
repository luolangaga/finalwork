package com.library.model.policy;

public class TeacherBorrowPolicy implements BorrowPolicy {

    @Override
    public int getMaxBorrowCount() {
        return 10;
    }

    @Override
    public int getMaxBorrowDays(String resourceType) {
        return switch (resourceType) {
            case "BOOK" -> 60;
            case "MAGAZINE" -> 30;
            case "DVD" -> 14;
            case "EBOOK" -> 42;
            default -> 30;
        };
    }

    @Override
    public boolean canRenew() {
        return true;
    }

    @Override
    public int getMaxRenewTimes() {
        return 2;
    }
}