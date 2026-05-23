package com.library.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("DVD")
public class DVD extends LibraryResource {

    private String director;
    private Integer durationMinutes;
    private String genre;

    protected DVD() {}

    public DVD(String id, String title, String director,
               int durationMinutes, String genre) {
        super(id, title);
        this.director = director;
        this.durationMinutes = durationMinutes;
        this.genre = genre;
    }

    @Override
    public int getMaxBorrowDays() {
        return 7;
    }

    @Override
    public String getResourceType() {
        return "DVD";
    }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }
    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }
    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }
}
