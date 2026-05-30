package com.library;

import com.library.model.factory.ResourceFactory;
import com.library.model.entity.*;
import com.library.model.policy.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.Map;

class PolicyAndFactoryTest {

    @Test
    void testFactoryDynamicRegistration() {
        ResourceFactory factory = new ResourceFactory();
        assertTrue(factory.isTypeSupported("BOOK"));
        assertTrue(factory.isTypeSupported("MAGAZINE"));
        assertTrue(factory.isTypeSupported("DVD"));
        assertTrue(factory.isTypeSupported("EBOOK"));
        assertEquals(4, factory.getSupportedTypes().size());
    }

    @Test
    void testFactoryUnknownType() {
        ResourceFactory factory = new ResourceFactory();
        assertThrows(IllegalArgumentException.class, () ->
                factory.createResource("UNKNOWN", new HashMap<>()));
    }

    @Test
    void testStudentPolicy() {
        StudentBorrowPolicy policy = new StudentBorrowPolicy();
        assertEquals(5, policy.getMaxBorrowCount());
        assertEquals(30, policy.getMaxBorrowDays("BOOK"));
        assertEquals(14, policy.getMaxBorrowDays("MAGAZINE"));
        assertEquals(7, policy.getMaxBorrowDays("DVD"));
        assertEquals(21, policy.getMaxBorrowDays("EBOOK"));
        assertTrue(policy.canRenew());
        assertEquals(1, policy.getMaxRenewTimes());
    }

    @Test
    void testTeacherPolicy() {
        TeacherBorrowPolicy policy = new TeacherBorrowPolicy();
        assertEquals(10, policy.getMaxBorrowCount());
        assertEquals(60, policy.getMaxBorrowDays("BOOK"));
        assertEquals(30, policy.getMaxBorrowDays("MAGAZINE"));
        assertEquals(14, policy.getMaxBorrowDays("DVD"));
        assertTrue(policy.canRenew());
        assertEquals(2, policy.getMaxRenewTimes());
    }

    @Test
    void testPublicPolicy() {
        PublicBorrowPolicy policy = new PublicBorrowPolicy();
        assertEquals(3, policy.getMaxBorrowCount());
        assertEquals(15, policy.getMaxBorrowDays("BOOK"));
        assertFalse(policy.canRenew());
        assertEquals(0, policy.getMaxRenewTimes());
    }

    @Test
    void testCreateMagazineViaFactory() {
        ResourceFactory factory = new ResourceFactory();
        Map<String, String> params = new HashMap<>();
        params.put("id", "M001");
        params.put("title", "测试杂志");
        params.put("issueNumber", "2024-01");
        params.put("publishDate", "2024-01-15");
        params.put("category", "科技");

        LibraryResource resource = factory.createResource("MAGAZINE", params);
        assertInstanceOf(Magazine.class, resource);
        assertEquals("测试杂志", resource.getTitle());
    }

    @Test
    void testCreateDVDViaFactory() {
        ResourceFactory factory = new ResourceFactory();
        Map<String, String> params = new HashMap<>();
        params.put("id", "D001");
        params.put("title", "测试DVD");
        params.put("director", "测试导演");
        params.put("duration", "120");
        params.put("genre", "科幻");

        LibraryResource resource = factory.createResource("DVD", params);
        assertInstanceOf(DVD.class, resource);
        assertEquals("测试DVD", resource.getTitle());
    }
}
