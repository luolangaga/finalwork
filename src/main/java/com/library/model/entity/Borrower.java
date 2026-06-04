package com.library.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "borrower")
public class Borrower {

    @Id
    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String email;

    @Column(nullable = false)
    private String type = "STUDENT";

    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowRecord> borrowHistory = new ArrayList<>();

    protected Borrower() {}

    public Borrower(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    public Borrower(String id, String name, String phone, String email, String type) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.type = type;
    }

    public int getCurrentBorrowCount() {
        return (int) borrowHistory.stream()
                .filter(r -> !r.isReturned()).count();
    }

    public void addBorrowRecord(BorrowRecord record) {
        borrowHistory.add(record);
        record.setBorrower(this);
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public List<BorrowRecord> getBorrowHistory() { return borrowHistory; }
    public void setBorrowHistory(List<BorrowRecord> borrowHistory) { this.borrowHistory = borrowHistory; }
}
