package com.library.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EBOOK")
public class EBook extends LibraryResource {

    protected EBook() {}

    public EBook(String id, String title) {
        super(id, title, "EBOOK");
    }

    @Override
    public int getMaxBorrowDays() {
        return 21;
    }
}
