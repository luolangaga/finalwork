package com.library.repository;

import com.library.model.entity.LibraryResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceRepository extends JpaRepository<LibraryResource, String> {

    @Query("SELECT r FROM LibraryResource r WHERE TYPE(r) = :type")
    List<LibraryResource> findByResourceType(@Param("type") String type);

    List<LibraryResource> findByTitleContaining(String keyword);

    List<LibraryResource> findByStatus(LibraryResource.ResourceStatus status);
}
