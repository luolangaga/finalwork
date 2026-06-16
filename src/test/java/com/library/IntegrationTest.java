package com.library;

import com.library.model.entity.*;
import com.library.model.factory.ResourceFactory;
import com.library.model.dto.ResourceDTO;
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
        Book book = new Book("B001", "Java编程思想");
        Map<String, Object> attrs = new HashMap<>();
        attrs.put("author", "Bruce Eckel");
        attrs.put("isbn", "9787111213826");
        attrs.put("publisher", "机械工业出版社");
        attrs.put("pages", 880);
        book.setExtraAttrs(attrs);
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
        Book book = new Book("B001", "书籍1");
        book.setExtraAttrs(Map.of("author", "作者1"));
        Magazine mag = new Magazine("M001", "杂志1");
        mag.setExtraAttrs(Map.of("category", "科技"));
        DVD dvd = new DVD("D001", "DVD1");
        dvd.setExtraAttrs(Map.of("director", "导演1"));
        EBook ebook = new EBook("E001", "电子书1");
        ebook.setExtraAttrs(Map.of("format", "PDF"));

        manager.addResource(book);
        manager.addResource(mag);
        manager.addResource(dvd);
        manager.addResource(ebook);

        List<LibraryResource> all = manager.getAllResources();
        assertEquals(4, all.size());

        List<LibraryResource> books = manager.findByType("BOOK");
        assertEquals(1, books.size());
    }

    @Test
    void testCollectionGrouping() {
        List<LibraryResource> resources = new ArrayList<>();
        Book b1 = new Book("B001", "书籍1");
        b1.setExtraAttrs(Map.of("author", "作者1"));
        Book b2 = new Book("B002", "书籍2");
        b2.setExtraAttrs(Map.of("author", "作者2"));
        Magazine m1 = new Magazine("M001", "杂志1");
        m1.setExtraAttrs(Map.of("category", "科技"));

        resources.add(b1);
        resources.add(b2);
        resources.add(m1);

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
        ResourceDTO dto = new ResourceDTO();
        dto.setId("C001");
        dto.setTitle("自定义资源");
        dto.setType("BOOK");
        dto.setExtraAttrs(Map.of("author", "custom", "isbn", "custom-isbn"));

        LibraryResource resource = ResourceFactory.create(dto);
        assertNotNull(resource);
        assertEquals("自定义资源", resource.getTitle());
        assertNotNull(resource.getExtraAttrs());
    }
}