package com.library.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.time.LocalDate;

@Entity
@DiscriminatorValue("MAGAZINE")
public class Magazine extends LibraryResource {

    private String issueNumber;
    private LocalDate publishDate;
    private String category;

    protected Magazine() {}

    public Magazine(String id, String title, String issueNumber,
                    LocalDate publishDate, String category) {
        super(id, title);
        this.issueNumber = issueNumber;
        this.publishDate = publishDate;
        this.category = category;
    }

    @Override
    public int getMaxBorrowDays() {
        return 14;
    }

    @Override
    public String getResourceType() {
        return "MAGAZINE";
    }

    public String getIssueNumber() { return issueNumber; }
    public void setIssueNumber(String issueNumber) { this.issueNumber = issueNumber; }
    public LocalDate getPublishDate() { return publishDate; }
    public void setPublishDate(LocalDate publishDate) { this.publishDate = publishDate; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
