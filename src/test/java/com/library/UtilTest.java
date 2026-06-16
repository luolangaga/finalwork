package com.library;

import com.library.model.entity.*;
import com.library.util.CollectionUtils;
import com.library.util.DateUtil;
import com.library.util.ValidationUtil;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class UtilTest {

    @Test
    void testCollectionUtils() {
        List<LibraryResource> resources = new ArrayList<>();
        Book b = new Book("B001", "Java编程思想");
        b.setExtraAttrs(Map.of("author", "Bruce Eckel"));
        Magazine m = new Magazine("M001", "自然杂志");
        m.setExtraAttrs(Map.of("category", "科技"));
        DVD d = new DVD("D001", "星际穿越");
        d.setExtraAttrs(Map.of("director", "Nolan"));

        resources.add(b);
        resources.add(m);
        resources.add(d);

        Map<String, Long> counts = CollectionUtils.countByType(resources);
        assertEquals(1L, counts.get("BOOK"));
        assertEquals(1L, counts.get("MAGAZINE"));
        assertEquals(1L, counts.get("DVD"));
    }

    @Test
    void testDateUtil() {
        LocalDate dueDate = LocalDate.now().minusDays(5);
        assertTrue(DateUtil.isOverdue(dueDate));

        LocalDate futureDate = LocalDate.now().plusDays(5);
        assertFalse(DateUtil.isOverdue(futureDate));

        String formatted = DateUtil.format(LocalDate.of(2024, 1, 15));
        assertEquals("2024-01-15", formatted);
    }

    @Test
    void testValidationUtil() {
        assertTrue(ValidationUtil.isValidEmail("test@example.com"));
        assertFalse(ValidationUtil.isValidEmail("invalid-email"));
    }

    @Test
    void testBorrowRecordOverdue() {
        BorrowRecord record = new BorrowRecord("R001", "U001",
                "B001", LocalDate.now().minusDays(40),
                LocalDate.now().minusDays(10));
        assertTrue(record.isOverdue());
        assertTrue(record.getOverdueDays() > 0);
    }

    @Test
    void testBorrowRecordReturn() {
        BorrowRecord record = new BorrowRecord("R001", "U001",
                "B001", LocalDate.now(),
                LocalDate.now().plusDays(30));
        assertFalse(record.isReturned());
        record.doReturn();
        assertTrue(record.isReturned());
    }
}