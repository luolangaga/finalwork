package com.library.controller;

import com.library.model.entity.BorrowRecord;
import com.library.service.BorrowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/borrow")
@CrossOrigin(origins = "*")
public class BorrowController {

    @Autowired
    private BorrowService borrowService;

    @PostMapping("/borrow")
    public ResponseEntity<BorrowRecord> borrowResource(
            @RequestParam String borrowerId,
            @RequestParam String resourceId) {
        return ResponseEntity.ok(
                borrowService.borrowResource(borrowerId, resourceId));
    }

    @PostMapping("/return")
    public ResponseEntity<Void> returnResource(
            @RequestParam String resourceId) {
        borrowService.returnResource(resourceId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/records/{borrowerId}")
    public ResponseEntity<List<BorrowRecord>> getBorrowRecords(
            @PathVariable String borrowerId) {
        return ResponseEntity.ok(
                borrowService.getBorrowRecords(borrowerId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<BorrowRecord>> getOverdueRecords() {
        return ResponseEntity.ok(borrowService.getOverdueRecords());
    }
}
