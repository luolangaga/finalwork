package com.library;

import com.library.model.factory.ResourceFactory;
import com.library.model.dto.ResourceDTO;
import com.library.model.entity.*;
import com.library.model.policy.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

class PolicyAndFactoryTest {

    @Test
    void testFactoryDynamicRegistration() {
        assertTrue(ResourceFactory.isTypeSupported("BOOK"));
        assertTrue(ResourceFactory.isTypeSupported("MAGAZINE"));
        assertTrue(ResourceFactory.isTypeSupported("DVD"));
        assertTrue(ResourceFactory.isTypeSupported("EBOOK"));
    }

    @Test
    void testFactoryUnknownType() {
        ResourceDTO dto = new ResourceDTO();
        dto.setType("UNKNOWN");
        assertThrows(IllegalArgumentException.class, () ->
                ResourceFactory.create(dto));
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
        ResourceDTO dto = new ResourceDTO();
        dto.setId("M001");
        dto.setTitle("测试杂志");
        dto.setType("MAGAZINE");
        dto.setExtraAttrs(Map.of(
                "issueNumber", "2024-01",
                "publishDate", "2024-01-15",
                "category", "科技"));

        LibraryResource resource = ResourceFactory.create(dto);
        assertInstanceOf(Magazine.class, resource);
        assertEquals("测试杂志", resource.getTitle());
        assertNotNull(resource.getExtraAttrs());
    }

    @Test
    void testCreateDVDViaFactory() {
        ResourceDTO dto = new ResourceDTO();
        dto.setId("D001");
        dto.setTitle("测试DVD");
        dto.setType("DVD");
        dto.setExtraAttrs(Map.of(
                "director", "测试导演",
                "durationMinutes", 120,
                "genre", "科幻"));

        LibraryResource resource = ResourceFactory.create(dto);
        assertInstanceOf(DVD.class, resource);
        assertEquals("测试DVD", resource.getTitle());
        assertNotNull(resource.getExtraAttrs());
    }
}