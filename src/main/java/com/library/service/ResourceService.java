package com.library.service;

import com.library.model.dto.ResourceDTO;
import com.library.model.entity.*;
import com.library.model.factory.ResourceFactory;
import com.library.repository.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepo;

    private final ResourceFactory factory = new ResourceFactory();

    public List<ResourceDTO> findAll() {
        return resourceRepo.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ResourceDTO findById(String id) {
        LibraryResource resource = resourceRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("资源不存在"));
        return convertToDTO(resource);
    }

    public List<ResourceDTO> findByType(String type) {
        return resourceRepo.findByResourceType(type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ResourceDTO> searchByKeyword(String keyword) {
        return resourceRepo.findAll().stream()
                .filter(r -> r.getTitle().contains(keyword))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ResourceDTO addResource(ResourceDTO dto) {
        Map<String, String> params = new HashMap<>();
        params.put("id", dto.getId());
        params.put("title", dto.getTitle());
        if (dto.getAuthor() != null) params.put("author", dto.getAuthor());
        if (dto.getIsbn() != null) params.put("isbn", dto.getIsbn());
        if (dto.getPublisher() != null) params.put("publisher", dto.getPublisher());
        if (dto.getPages() != null) params.put("pages", String.valueOf(dto.getPages()));
        if (dto.getIssueNumber() != null) params.put("issueNumber", dto.getIssueNumber());
        if (dto.getPublishDate() != null) params.put("publishDate", dto.getPublishDate());
        if (dto.getCategory() != null) params.put("category", dto.getCategory());
        if (dto.getDirector() != null) params.put("director", dto.getDirector());
        if (dto.getDurationMinutes() != null) params.put("duration", String.valueOf(dto.getDurationMinutes()));
        if (dto.getGenre() != null) params.put("genre", dto.getGenre());
        if (dto.getFormat() != null) params.put("format", dto.getFormat());
        if (dto.getFileSizeMB() != null) params.put("fileSizeMB", String.valueOf(dto.getFileSizeMB()));
        if (dto.getDownloadUrl() != null) params.put("downloadUrl", dto.getDownloadUrl());

        LibraryResource resource = factory.createResource(dto.getType(), params);
        resourceRepo.save(resource);
        return convertToDTO(resource);
    }

    @Transactional
    public void deleteResource(String id) {
        resourceRepo.deleteById(id);
    }

    private ResourceDTO convertToDTO(LibraryResource r) {
        ResourceDTO dto = new ResourceDTO();
        dto.setId(r.getId());
        dto.setTitle(r.getTitle());
        dto.setType(r.getResourceType());
        dto.setStatus(r.getStatus().name());
        dto.setBorrowerId(r.getBorrowerId());
        if (r.getBorrowDate() != null) dto.setBorrowDate(r.getBorrowDate().toString());
        if (r.getDueDate() != null) dto.setDueDate(r.getDueDate().toString());

        if (r instanceof Book book) {
            dto.setAuthor(book.getAuthor());
            dto.setIsbn(book.getIsbn());
            dto.setPublisher(book.getPublisher());
            dto.setPages(book.getPages());
        } else if (r instanceof Magazine mag) {
            dto.setIssueNumber(mag.getIssueNumber());
            if (mag.getPublishDate() != null) dto.setPublishDate(mag.getPublishDate().toString());
            dto.setCategory(mag.getCategory());
        } else if (r instanceof DVD dvd) {
            dto.setDirector(dvd.getDirector());
            dto.setDurationMinutes(dvd.getDurationMinutes());
            dto.setGenre(dvd.getGenre());
        } else if (r instanceof EBook ebook) {
            dto.setFormat(ebook.getFormat());
            dto.setFileSizeMB(ebook.getFileSizeMB());
            dto.setDownloadUrl(ebook.getDownloadUrl());
        }
        return dto;
    }
}
