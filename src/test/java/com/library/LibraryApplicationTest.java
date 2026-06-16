package com.library;

import com.library.model.entity.*;
import com.library.model.factory.ResourceFactory;
import com.library.model.dto.ResourceDTO;
import com.library.model.policy.*;
import com.library.manager.ResourceManager;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.Map;

class LibraryApplicationTest {

    @Test
    void testBookCreation() {
        Book book = new Book("B001", "Java编程思想");
        book.setExtraAttrs(Map.of(
                "author", "Bruce Eckel",
                "isbn", "9787111213826",
                "publisher", "机械工业出版社",
                "pages", 880));
        assertEquals("BOOK", book.getType());
        assertEquals(30, book.getMaxBorrowDays());
        assertEquals("Bruce Eckel", book.getExtraAttrs().get("author"));
    }

    @Test
    void testMagazineCreation() {
        Magazine mag = new Magazine("M001", "自然杂志");
        mag.setExtraAttrs(Map.of(
                "issueNumber", "2024-01",
                "publishDate", "2024-01-15",
                "category", "科技"));
        assertEquals("MAGAZINE", mag.getType());
        assertEquals(14, mag.getMaxBorrowDays());
    }

    @Test
    void testDVDCreation() {
        DVD dvd = new DVD("D001", "星际穿越");
        dvd.setExtraAttrs(Map.of(
                "director", "Christopher Nolan",
                "durationMinutes", 169,
                "genre", "科幻"));
        assertEquals("DVD", dvd.getType());
        assertEquals(7, dvd.getMaxBorrowDays());
    }

    @Test
    void testEBookCreation() {
        EBook ebook = new EBook("E001", "算法导论");
        ebook.setExtraAttrs(Map.of(
                "format", "PDF",
                "fileSizeMB", 45,
                "downloadUrl", "https://example.com/clrs.pdf"));
        assertEquals("EBOOK", ebook.getType());
        assertEquals(21, ebook.getMaxBorrowDays());
    }

    @Test
    void testBorrowAndReturn() {
        Book book = new Book("B001", "测试书籍");
        assertTrue(book.borrow("U001"));
        assertEquals(LibraryResource.ResourceStatus.BORROWED, book.getStatus());
        assertTrue(book.returnResource());
        assertEquals(LibraryResource.ResourceStatus.AVAILABLE, book.getStatus());
    }

    @Test
    void testBorrowerWithType() {
        Borrower borrower = new Borrower("U001", "张三",
                "13800138001", "test@example.com", "STUDENT");
        assertEquals("STUDENT", borrower.getType());
    }

    @Test
    void testResourceFactory() {
        ResourceDTO dto = new ResourceDTO();
        dto.setId("B001");
        dto.setTitle("测试书籍");
        dto.setType("BOOK");
        dto.setExtraAttrs(Map.of(
                "author", "作者",
                "isbn", "1234567890",
                "publisher", "出版社",
                "pages", 100));

        LibraryResource resource = ResourceFactory.create(dto);
        assertInstanceOf(Book.class, resource);
        assertEquals("测试书籍", resource.getTitle());
        assertEquals("作者", resource.getExtraAttrs().get("author"));
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
        Book book = new Book("B001", "测试书籍");
        book.setExtraAttrs(Map.of("author", "作者"));
        manager.addResource(book);

        assertEquals(book, manager.findById("B001"));
        assertEquals(1, manager.getResourceCount());
        assertTrue(manager.containsResource("B001"));

        manager.removeResource("B001");
        assertNull(manager.findById("B001"));
    }
}