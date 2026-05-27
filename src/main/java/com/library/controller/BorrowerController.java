package com.library.controller;

import com.library.model.entity.Borrower;
import com.library.service.BorrowerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrowers")
@CrossOrigin(origins = "*")
public class BorrowerController {

    @Autowired
    private BorrowerService borrowerService;

    @GetMapping
    public ResponseEntity<List<Borrower>> getAllBorrowers() {
        return ResponseEntity.ok(borrowerService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrower> getBorrower(@PathVariable String id) {
        return ResponseEntity.ok(borrowerService.findById(id));
    }

    @PostMapping
    public ResponseEntity<Borrower> addBorrower(
            @RequestBody Borrower borrower) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(borrowerService.addBorrower(borrower));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrower(@PathVariable String id) {
        borrowerService.deleteBorrower(id);
        return ResponseEntity.noContent().build();
    }
}
