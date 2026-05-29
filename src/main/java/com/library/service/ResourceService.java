package com.library.service;

import com.library.config.RabbitMQConfig;
import com.library.model.dto.ResourceDTO;
import com.library.model.entity.*;
import com.library.model.factory.ResourceFactory;
import com.library.repository.ResourceRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ResourceService {

    @Autowired
    private ResourceRepository resourceRepo;

    @Autowired(required = false)
    private RabbitTemplate rabbitTemplate;

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
        LibraryResource resource = ResourceFactory.create(dto);
        resourceRepo.save(resource);

        if (rabbitTemplate != null) {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.LIBRARY_EXCHANGE,
                    "resource.created",
                    java.util.Map.of("resourceId", resource.getId(),
                            "type", resource.getType())
            );
        }

        return convertToDTO(resource);
    }

    @Transactional
    public void deleteResource(String id) {
        resourceRepo.deleteById(id);

        if (rabbitTemplate != null) {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.LIBRARY_EXCHANGE,
                    "resource.deleted",
                    java.util.Map.of("resourceId", id)
            );
        }
    }

    private ResourceDTO convertToDTO(LibraryResource r) {
        ResourceDTO dto = new ResourceDTO();
        dto.setId(r.getId());
        dto.setTitle(r.getTitle());
        dto.setType(r.getType());
        dto.setStatus(r.getStatus().name());
        dto.setBorrowerId(r.getBorrowerId());
        if (r.getBorrowDate() != null) dto.setBorrowDate(r.getBorrowDate().toString());
        if (r.getDueDate() != null) dto.setDueDate(r.getDueDate().toString());
        dto.setBorrowDays(r.getMaxBorrowDays());
        dto.setExtraAttrs(r.getExtraAttrs());
        return dto;
    }
}