package com.library.model.factory;

import com.library.model.dto.ResourceDTO;
import com.library.model.entity.*;
import java.util.HashMap;
import java.util.Map;

public class ResourceFactory {

    public static LibraryResource create(ResourceDTO dto) {
        String type = dto.getType();
        Map<String, Object> attrs = dto.getExtraAttrs() != null
                ? dto.getExtraAttrs() : new HashMap<>();

        LibraryResource resource = switch (type) {
            case "BOOK" -> new Book(dto.getId(), dto.getTitle());
            case "MAGAZINE" -> new Magazine(dto.getId(), dto.getTitle());
            case "DVD" -> new DVD(dto.getId(), dto.getTitle());
            case "EBOOK" -> new EBook(dto.getId(), dto.getTitle());
            default -> throw new IllegalArgumentException("未知资源类型: " + type);
        };

        Map<String, Object> extra = new HashMap<>();
        if ("BOOK".equals(type)) {
            if (attrs.containsKey("author")) extra.put("author", attrs.get("author"));
            if (attrs.containsKey("isbn")) extra.put("isbn", attrs.get("isbn"));
            if (attrs.containsKey("publisher")) extra.put("publisher", attrs.get("publisher"));
            if (attrs.containsKey("pages")) extra.put("pages", attrs.get("pages"));
        } else if ("MAGAZINE".equals(type)) {
            if (attrs.containsKey("issueNumber")) extra.put("issueNumber", attrs.get("issueNumber"));
            if (attrs.containsKey("publishDate")) extra.put("publishDate", attrs.get("publishDate"));
            if (attrs.containsKey("category")) extra.put("category", attrs.get("category"));
        } else if ("DVD".equals(type)) {
            if (attrs.containsKey("director")) extra.put("director", attrs.get("director"));
            if (attrs.containsKey("durationMinutes")) extra.put("durationMinutes", attrs.get("durationMinutes"));
            if (attrs.containsKey("genre")) extra.put("genre", attrs.get("genre"));
        } else if ("EBOOK".equals(type)) {
            if (attrs.containsKey("format")) extra.put("format", attrs.get("format"));
            if (attrs.containsKey("fileSizeMB")) extra.put("fileSizeMB", attrs.get("fileSizeMB"));
            if (attrs.containsKey("downloadUrl")) extra.put("downloadUrl", attrs.get("downloadUrl"));
        }

        resource.setExtraAttrs(extra);
        return resource;
    }

    public static boolean isTypeSupported(String type) {
        return "BOOK".equals(type) || "MAGAZINE".equals(type)
                || "DVD".equals(type) || "EBOOK".equals(type);
    }
}