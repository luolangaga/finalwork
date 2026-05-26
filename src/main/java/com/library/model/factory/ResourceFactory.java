package com.library.model.factory;

import com.library.model.entity.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ResourceFactory {

    private final Map<String, ResourceCreator> creators;

    @FunctionalInterface
    public interface ResourceCreator {
        LibraryResource create(Map<String, String> params);
    }

    public ResourceFactory() {
        this.creators = new HashMap<>();
        register("BOOK", this::createBook);
        register("MAGAZINE", this::createMagazine);
        register("DVD", this::createDVD);
        register("EBOOK", this::createEBook);
    }

    public void register(String type, ResourceCreator creator) {
        creators.put(type, creator);
    }

    public LibraryResource createResource(String type, Map<String, String> params) {
        ResourceCreator creator = creators.get(type);
        if (creator == null) {
            throw new IllegalArgumentException("未知资源类型: " + type);
        }
        return creator.create(params);
    }

    public boolean isTypeSupported(String type) {
        return creators.containsKey(type);
    }

    public Set<String> getSupportedTypes() {
        return creators.keySet();
    }

    private LibraryResource createBook(Map<String, String> p) {
        return new Book(p.get("id"), p.get("title"),
                p.get("author"), p.get("isbn"),
                p.get("publisher"),
                Integer.parseInt(p.getOrDefault("pages", "0")));
    }

    private LibraryResource createMagazine(Map<String, String> p) {
        return new Magazine(p.get("id"), p.get("title"),
                p.get("issueNumber"),
                LocalDate.parse(p.get("publishDate")),
                p.get("category"));
    }

    private LibraryResource createDVD(Map<String, String> p) {
        return new DVD(p.get("id"), p.get("title"),
                p.get("director"),
                Integer.parseInt(p.getOrDefault("duration", "0")),
                p.get("genre"));
    }

    private LibraryResource createEBook(Map<String, String> p) {
        return new EBook(p.get("id"), p.get("title"),
                p.get("format"),
                Long.parseLong(p.getOrDefault("fileSizeMB", "0")),
                p.get("downloadUrl"));
    }
}
