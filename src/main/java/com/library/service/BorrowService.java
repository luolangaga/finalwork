package com.library.service;

import com.library.model.entity.*;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.BorrowerRepository;
import com.library.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BorrowService {

    @Autowired
    private ResourceRepository resourceRepo;
    @Autowired
    private BorrowerRepository borrowerRepo;
    @Autowired
    private BorrowRecordRepository recordRepo;

    @Transactional
    public BorrowRecord borrowResource(String borrowerId, String resourceId) {
        LibraryResource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("资源不存在"));
        Borrower borrower = borrowerRepo.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("借阅者不存在"));

        if (resource.getStatus() != LibraryResource.ResourceStatus.AVAILABLE)
            throw new RuntimeException("资源不可借");
        if (!borrower.canBorrow())
            throw new RuntimeException("借阅数量已达上限");
        if (!borrower.getOverdueRecords().isEmpty())
            throw new RuntimeException("有逾期未还资源");

        resource.borrow(borrowerId);
        BorrowRecord record = new BorrowRecord(
                UUID.randomUUID().toString(), borrowerId, resourceId,
                LocalDate.now(),
                LocalDate.now().plusDays(resource.getMaxBorrowDays()));
        borrower.addBorrowRecord(record);

        resourceRepo.save(resource);
        recordRepo.save(record);
        borrowerRepo.save(borrower);

        return record;
    }

    @Transactional
    public void returnResource(String resourceId) {
        LibraryResource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("资源不存在"));
        resource.returnResource();
        resourceRepo.save(resource);
    }

    public List<BorrowRecord> getBorrowRecords(String borrowerId) {
        Borrower borrower = borrowerRepo.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("借阅者不存在"));
        return borrower.getBorrowHistory();
    }

    public List<BorrowRecord> getOverdueRecords() {
        return recordRepo.findAll().stream()
                .filter(BorrowRecord::isOverdue)
                .collect(Collectors.toList());
    }
}
