package com.library.model.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "borrower")
public class Borrower {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String name;

    private String phone;
    private String email;

    @OneToMany(mappedBy = "borrower", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BorrowRecord> borrowHistory = new ArrayList<>();

    private static final int MAX_BORROW_LIMIT = 5;

    protected Borrower() {}

    public Borrower(String id, String name, String phone, String email) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.borrowHistory = new ArrayList<>();
    }

    public int getCurrentBorrowCount() {
        return (int) borrowHistory.stream()
                .filter(r -> !r.isReturned()).count();
    }

    public boolean canBorrow() {
        return getCurrentBorrowCount() < MAX_BORROW_LIMIT;
    }

    public void addBorrowRecord(BorrowRecord record) {
        borrowHistory.add(record);
        record.setBorrower(this);
    }

    public List<BorrowRecord> getOverdueRecords() {
        return borrowHistory.stream()
                .filter(r -> !r.isReturned() && r.isOverdue())
                .collect(Collectors.toList());
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public List<BorrowRecord> getBorrowHistory() { return borrowHistory; }
    public void setBorrowHistory(List<BorrowRecord> borrowHistory) { this.borrowHistory = borrowHistory; }
}
