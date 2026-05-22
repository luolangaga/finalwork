package com.library.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BOOK")
public class Book extends LibraryResource {

    private String author;
    private String isbn;
    private String publisher;
    private Integer pages;

    protected Book() {}

    public Book(String id, String title, String author,
                String isbn, String publisher, int pages) {
        super(id, title);
        this.author = author;
        this.isbn = isbn;
        this.publisher = publisher;
        this.pages = pages;
    }

    @Override
    public int getMaxBorrowDays() {
        return 30;
    }

    @Override
    public String getResourceType() {
        return "BOOK";
    }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public Integer getPages() { return pages; }
    public void setPages(Integer pages) { this.pages = pages; }
}
