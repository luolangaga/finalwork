package com.library.model.policy;

public class PublicBorrowPolicy implements BorrowPolicy {

    @Override
    public int getMaxBorrowCount() {
        return 3;
    }

    @Override
    public int getMaxBorrowDays(String resourceType) {
        return switch (resourceType) {
            case "BOOK" -> 15;
            case "MAGAZINE" -> 7;
            case "DVD" -> 3;
            case "EBOOK" -> 10;
            default -> 7;
        };
    }

    @Override
    public boolean canRenew() {
        return false;
    }

    @Override
    public int getMaxRenewTimes() {
        return 0;
    }
}
