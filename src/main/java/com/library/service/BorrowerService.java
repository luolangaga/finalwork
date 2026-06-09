package com.library.service;

import com.library.model.entity.Borrower;
import com.library.repository.BorrowerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BorrowerService {

    @Autowired
    private BorrowerRepository borrowerRepo;

    public List<Borrower> findAll() {
        return borrowerRepo.findAll();
    }

    public Borrower findById(String id) {
        return borrowerRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("借阅者不存在"));
    }

    @Transactional
    public Borrower addBorrower(Borrower borrower) {
        return borrowerRepo.save(borrower);
    }

    @Transactional
    public void deleteBorrower(String id) {
        borrowerRepo.deleteById(id);
    }

    @Transactional
    public Borrower updateBorrower(String id, Borrower updated) {
        Borrower borrower = findById(id);
        borrower.setName(updated.getName());
        borrower.setPhone(updated.getPhone());
        borrower.setEmail(updated.getEmail());
        return borrowerRepo.save(borrower);
    }
}