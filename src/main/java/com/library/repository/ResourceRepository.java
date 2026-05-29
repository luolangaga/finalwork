package com.library.repository;

import com.library.model.entity.LibraryResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<LibraryResource, String> {
    List<LibraryResource> findByResourceType(String resourceType);
    List<LibraryResource> findByTitleContaining(String keyword);
    List<LibraryResource> findByStatus(LibraryResource.ResourceStatus status);
}
