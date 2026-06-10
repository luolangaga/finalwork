package com.library.controller;

import com.library.model.dto.ResourceDTO;
import com.library.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
@CrossOrigin(origins = "*")
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping
    public ResponseEntity<List<ResourceDTO>> getAllResources() {
        return ResponseEntity.ok(resourceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResourceDTO> getResource(@PathVariable String id) {
        return ResponseEntity.ok(resourceService.findById(id));
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<ResourceDTO>> getByType(@PathVariable String type) {
        return ResponseEntity.ok(resourceService.findByType(type));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ResourceDTO>> searchResources(
            @RequestParam String keyword) {
        return ResponseEntity.ok(resourceService.searchByKeyword(keyword));
    }

    @PostMapping
    public ResponseEntity<ResourceDTO> addResource(
            @RequestBody ResourceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(resourceService.addResource(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable String id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }
}