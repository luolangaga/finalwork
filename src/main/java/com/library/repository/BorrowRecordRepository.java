package com.library.repository;

import com.library.model.entity.BorrowRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, String> {
    List<BorrowRecord> findByResourceId(String resourceId);
    List<BorrowRecord> findByReturnedFalse();
}
