package com.library.service;

import com.library.config.RabbitMQConfig;
import com.library.model.entity.*;
import com.library.model.policy.*;
import com.library.repository.BorrowRecordRepository;
import com.library.repository.BorrowerRepository;
import com.library.repository.ResourceRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
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
    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

    private final Map<String, BorrowPolicy> policies = Map.of(
            "STUDENT", new StudentBorrowPolicy(),
            "TEACHER", new TeacherBorrowPolicy(),
            "PUBLIC", new PublicBorrowPolicy()
    );

    public BorrowPolicy getPolicy(String borrowerType) {
        return policies.getOrDefault(borrowerType, policies.get("PUBLIC"));
    }

    @Transactional
    public BorrowRecord borrowResource(String borrowerId, String resourceId) {
        LibraryResource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("资源不存在"));
        Borrower borrower = borrowerRepo.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("借阅者不存在"));

        BorrowPolicy policy = getPolicy(borrower.getType());

        if (resource.getStatus() != LibraryResource.ResourceStatus.AVAILABLE)
            throw new RuntimeException("资源不可借");
        if (borrower.getCurrentBorrowCount() >= policy.getMaxBorrowCount())
            throw new RuntimeException("借阅数量已达上限");

        int borrowDays = policy.getMaxBorrowDays(resource.getType());
        resource.borrow(borrowerId);
        BorrowRecord record = new BorrowRecord(
                UUID.randomUUID().toString(), borrowerId, resourceId,
                LocalDate.now(),
                LocalDate.now().plusDays(borrowDays));
        borrower.addBorrowRecord(record);

        resourceRepo.save(resource);
        recordRepo.save(record);
        borrowerRepo.save(borrower);

        if (rabbitTemplate != null) {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.LIBRARY_EXCHANGE,
                    "borrow.created",
                    Map.of("borrowerId", borrowerId,
                            "resourceId", resourceId,
                            "borrowDate", LocalDate.now().toString(),
                            "dueDate", LocalDate.now().plusDays(borrowDays).toString())
            );
        }

        return record;
    }

    @Transactional
    public void returnResource(String resourceId) {
        LibraryResource resource = resourceRepo.findById(resourceId)
                .orElseThrow(() -> new RuntimeException("资源不存在"));
        resource.returnResource();
        resourceRepo.save(resource);

        if (rabbitTemplate != null) {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.LIBRARY_EXCHANGE,
                    "borrow.returned",
                    Map.of("resourceId", resourceId,
                            "returnDate", LocalDate.now().toString())
            );
        }
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