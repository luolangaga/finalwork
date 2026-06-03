package com.library.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BOOK")
public class Book extends LibraryResource {

    protected Book() {}

    public Book(String id, String title) {
        super(id, title, "BOOK");
    }

    @Override
    public int getMaxBorrowDays() {
        return 30;
    }
}
