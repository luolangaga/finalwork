package com.library.manager;

import com.library.model.entity.LibraryResource;
import java.util.*;
import java.util.stream.Collectors;

public class ResourceManager {

    private final Map<String, LibraryResource> resourceMap;
    private final List<LibraryResource> sortedList;

    public ResourceManager() {
        this.resourceMap = new HashMap<>();
        this.sortedList = new ArrayList<>();
    }

    public void addResource(LibraryResource resource) {
        resourceMap.put(resource.getId(), resource);
        if (!sortedList.contains(resource)) {
            sortedList.add(resource);
            sortedList.sort(Comparator.comparing(LibraryResource::getTitle)
                    .thenComparing(LibraryResource::getId));
        }
    }

    public LibraryResource findById(String id) {
        return resourceMap.get(id);
    }

    public List<LibraryResource> searchByTitle(String keyword) {
        return resourceMap.values().stream()
                .filter(r -> r.getTitle().contains(keyword))
                .collect(Collectors.toList());
    }

    public List<LibraryResource> findByType(String type) {
        return resourceMap.values().stream()
                .filter(r -> r.getResourceType().equals(type))
                .collect(Collectors.toList());
    }

    public List<LibraryResource> getAllResources() {
        return new ArrayList<>(sortedList);
    }

    public void removeResource(String id) {
        LibraryResource resource = resourceMap.remove(id);
        if (resource != null) {
            sortedList.remove(resource);
        }
    }

    public int getResourceCount() {
        return resourceMap.size();
    }

    public boolean containsResource(String id) {
        return resourceMap.containsKey(id);
    }
}
