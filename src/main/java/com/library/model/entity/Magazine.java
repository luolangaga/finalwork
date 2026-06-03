package com.library.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MAGAZINE")
public class Magazine extends LibraryResource {

    protected Magazine() {}

    public Magazine(String id, String title) {
        super(id, title, "MAGAZINE");
    }

    @Override
    public int getMaxBorrowDays() {
        return 14;
    }
}
