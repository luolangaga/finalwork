package com.library;

import com.library.model.entity.*;
import com.library.model.factory.ResourceFactory;
import com.library.model.policy.*;
import com.library.manager.ResourceManager;
import com.library.util.CollectionUtils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.*;

class IntegrationTest {

    @Test
    void testFullBorrowWorkflow() {
        ResourceManager manager = new ResourceManager();
        Book book = new Book("B001", "Java编程思想",
                "Bruce Eckel", "9787111213826",
                "机械工业出版社", 880);
        manager.addResource(book);

        LibraryResource found = manager.findById("B001");
        assertNotNull(found);
        assertEquals(LibraryResource.ResourceStatus.AVAILABLE, found.getStatus());

        assertTrue(found.borrow("U001"));
        assertEquals(LibraryResource.ResourceStatus.BORROWED, found.getStatus());
        assertNotNull(found.getDueDate());

        assertTrue(found.returnResource());
        assertEquals(LibraryResource.ResourceStatus.AVAILABLE, found.getStatus());
    }

    @Test
    void testMultipleResourceTypes() {
        ResourceManager manager = new ResourceManager();
        manager.addResource(new Book("B001", "书籍1", "作者1", "ISBN1", "出版社1", 100));
        manager.addResource(new Magazine("M001", "杂志1", "2024-01", LocalDate.now(), "科技"));
        manager.addResource(new DVD("D001", "DVD1", "导演1", 120, "科幻"));
        manager.addResource(new EBook("E001", "电子书1", "PDF", 10, "http://example.com"));

        List<LibraryResource> all = manager.getAllResources();
        assertEquals(4, all.size());

        List<LibraryResource> books = manager.findByType("BOOK");
        assertEquals(1, books.size());
    }

    @Test
    void testCollectionGrouping() {
        List<LibraryResource> resources = new ArrayList<>();
        resources.add(new Book("B001", "书籍1", "作者1", "ISBN1", "出版社1", 100));
        resources.add(new Book("B002", "书籍2", "作者2", "ISBN2", "出版社2", 200));
        resources.add(new Magazine("M001", "杂志1", "2024-01", LocalDate.now(), "科技"));

        Map<String, List<LibraryResource>> grouped =
                CollectionUtils.groupByType(resources);
        assertEquals(2, grouped.get("BOOK").size());
        assertEquals(1, grouped.get("MAGAZINE").size());
    }

    @Test
    void testPolicyHierarchy() {
        BorrowPolicy student = new StudentBorrowPolicy();
        BorrowPolicy teacher = new TeacherBorrowPolicy();
        BorrowPolicy pub = new PublicBorrowPolicy();

        assertTrue(student.getMaxBorrowCount() < teacher.getMaxBorrowCount());
        assertTrue(pub.getMaxBorrowCount() < student.getMaxBorrowCount());
        assertTrue(teacher.getMaxBorrowDays("BOOK") > student.getMaxBorrowDays("BOOK"));
    }

    @Test
    void testFactoryOCP() {
        ResourceFactory factory = new ResourceFactory();
        factory.register("CUSTOM", params ->
                new Book(params.get("id"), params.get("title"),
                        "custom", "custom-isbn", "custom-pub", 0));

        Map<String, String> params = new HashMap<>();
        params.put("id", "C001");
        params.put("title", "自定义资源");
        LibraryResource resource = factory.createResource("CUSTOM", params);
        assertNotNull(resource);
        assertEquals("自定义资源", resource.getTitle());
    }
}
