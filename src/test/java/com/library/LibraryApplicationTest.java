package com.library;

import com.library.model.entity.*;
import com.library.model.factory.ResourceFactory;
import com.library.model.policy.*;
import com.library.manager.ResourceManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

class LibraryApplicationTest {

    @Test
    void testBookCreation() {
        Book book = new Book("B001", "Java编程思想",
                "Bruce Eckel", "9787111213826",
                "机械工业出版社", 880);
        assertEquals("BOOK", book.getResourceType());
        assertEquals(30, book.getMaxBorrowDays());
    }

    @Test
    void testMagazineCreation() {
        Magazine mag = new Magazine("M001", "自然杂志",
                "2024-01", LocalDate.of(2024, 1, 15), "科技");
        assertEquals("MAGAZINE", mag.getResourceType());
        assertEquals(14, mag.getMaxBorrowDays());
    }

    @Test
    void testDVDCreation() {
        DVD dvd = new DVD("D001", "星际穿越",
                "Christopher Nolan", 169, "科幻");
        assertEquals("DVD", dvd.getResourceType());
        assertEquals(7, dvd.getMaxBorrowDays());
    }

    @Test
    void testEBookCreation() {
        EBook ebook = new EBook("E001", "算法导论",
                "PDF", 45, "https://example.com/clrs.pdf");
        assertEquals("EBOOK", ebook.getResourceType());
        assertEquals(21, ebook.getMaxBorrowDays());
    }

    @Test
    void testBorrowAndReturn() {
        Book book = new Book("B001", "测试书籍",
                "作者", "ISBN", "出版社", 100);
        assertTrue(book.borrow("U001"));
        assertEquals(LibraryResource.ResourceStatus.BORROWED, book.getStatus());
        assertTrue(book.returnResource());
        assertEquals(LibraryResource.ResourceStatus.AVAILABLE, book.getStatus());
    }

    @Test
    void testBorrowerLimit() {
        Borrower borrower = new Borrower("U001", "张三",
                "13800138001", "test@example.com");
        assertTrue(borrower.canBorrow());
    }

    @Test
    void testResourceFactory() {
        ResourceFactory factory = new ResourceFactory();
        Map<String, String> params = new HashMap<>();
        params.put("id", "B001");
        params.put("title", "测试书籍");
        params.put("author", "作者");
        params.put("isbn", "1234567890");
        params.put("publisher", "出版社");
        params.put("pages", "100");

        LibraryResource resource = factory.createResource("BOOK", params);
        assertInstanceOf(Book.class, resource);
        assertEquals("测试书籍", resource.getTitle());
    }

    @Test
    void testBorrowPolicy() {
        BorrowPolicy studentPolicy = new StudentBorrowPolicy();
        assertEquals(5, studentPolicy.getMaxBorrowCount());
        assertEquals(30, studentPolicy.getMaxBorrowDays("BOOK"));

        BorrowPolicy teacherPolicy = new TeacherBorrowPolicy();
        assertEquals(10, teacherPolicy.getMaxBorrowCount());
        assertEquals(60, teacherPolicy.getMaxBorrowDays("BOOK"));
    }

    @Test
    void testResourceManager() {
        ResourceManager manager = new ResourceManager();
        Book book = new Book("B001", "测试书籍",
                "作者", "ISBN", "出版社", 100);
        manager.addResource(book);

        assertEquals(book, manager.findById("B001"));
        assertEquals(1, manager.getResourceCount());
        assertTrue(manager.containsResource("B001"));

        manager.removeResource("B001");
        assertNull(manager.findById("B001"));
    }
}
