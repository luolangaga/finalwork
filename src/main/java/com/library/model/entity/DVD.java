package com.library.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DVD")
public class DVD extends LibraryResource {

    protected DVD() {}

    public DVD(String id, String title) {
        super(id, title, "DVD");
    }

    @Override
    public int getMaxBorrowDays() {
        return 7;
    }
}
