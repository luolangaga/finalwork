package com.library.util;

import com.library.model.entity.LibraryResource;
import java.util.*;
import java.util.stream.Collectors;

public class CollectionUtils {

    public static Map<String, List<LibraryResource>> groupByType(List<LibraryResource> resources) {
        return resources.stream()
                .collect(Collectors.groupingBy(LibraryResource::getResourceType));
    }

    public static Map<String, Long> countByType(List<LibraryResource> resources) {
        return resources.stream()
                .collect(Collectors.groupingBy(
                        LibraryResource::getResourceType,
                        Collectors.counting()));
    }

    public static List<LibraryResource> filterByStatus(
            List<LibraryResource> resources,
            LibraryResource.ResourceStatus status) {
        return resources.stream()
                .filter(r -> r.getStatus() == status)
                .collect(Collectors.toList());
    }

    public static List<LibraryResource> sortByTitle(List<LibraryResource> resources) {
        return resources.stream()
                .sorted(Comparator.comparing(LibraryResource::getTitle))
                .collect(Collectors.toList());
    }

    public static Optional<LibraryResource> findFirstAvailable(
            List<LibraryResource> resources) {
        return resources.stream()
                .filter(r -> r.getStatus() == LibraryResource.ResourceStatus.AVAILABLE)
                .findFirst();
    }

    public static Map<Boolean, List<LibraryResource>> partitionByAvailability(
            List<LibraryResource> resources) {
        return resources.stream()
                .collect(Collectors.partitioningBy(
                        r -> r.getStatus() == LibraryResource.ResourceStatus.AVAILABLE));
    }

    public static String generateStatistics(List<LibraryResource> resources) {
        Map<String, Long> counts = countByType(resources);
        StringBuilder sb = new StringBuilder();
        sb.append("资源统计:\n");
        counts.forEach((type, count) ->
                sb.append(String.format("  %s: %d\n", type, count)));
        sb.append(String.format("  总计: %d\n", resources.size()));
        return sb.toString();
    }
}
