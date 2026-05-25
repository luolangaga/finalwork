package com.library.model.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("EBOOK")
public class EBook extends LibraryResource {

    private String format;
    private Long fileSizeMB;
    private String downloadUrl;

    protected EBook() {}

    public EBook(String id, String title, String format,
                 long fileSizeMB, String downloadUrl) {
        super(id, title);
        this.format = format;
        this.fileSizeMB = fileSizeMB;
        this.downloadUrl = downloadUrl;
    }

    @Override
    public int getMaxBorrowDays() {
        return 21;
    }

    @Override
    public String getResourceType() {
        return "EBOOK";
    }

    public String getFormat() { return format; }
    public void setFormat(String format) { this.format = format; }
    public Long getFileSizeMB() { return fileSizeMB; }
    public void setFileSizeMB(Long fileSizeMB) { this.fileSizeMB = fileSizeMB; }
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
}
