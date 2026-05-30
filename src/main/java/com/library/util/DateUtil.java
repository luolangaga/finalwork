package com.library.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateUtil {

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String format(LocalDate date) {
        if (date == null) return "";
        return date.format(FORMATTER);
    }

    public static LocalDate parse(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) return null;
        return LocalDate.parse(dateStr, FORMATTER);
    }

    public static long daysBetween(LocalDate start, LocalDate end) {
        return ChronoUnit.DAYS.between(start, end);
    }

    public static long daysUntilNow(LocalDate date) {
        if (date == null) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }

    public static boolean isOverdue(LocalDate dueDate) {
        return dueDate != null && LocalDate.now().isAfter(dueDate);
    }

    public static LocalDate calculateDueDate(LocalDate borrowDate,
                                              int borrowDays) {
        if (borrowDate == null) return null;
        return borrowDate.plusDays(borrowDays);
    }

    public static String getOverdueInfo(LocalDate dueDate) {
        if (!isOverdue(dueDate)) return "未逾期";
        long days = ChronoUnit.DAYS.between(dueDate, LocalDate.now());
        return "已逾期 " + days + " 天";
    }
}
