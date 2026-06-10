package com.library.repository;

import com.library.model.entity.Borrower;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowerRepository extends JpaRepository<Borrower, String> {
    Borrower findByName(String name);
    boolean existsByEmail(String email);
}