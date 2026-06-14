package com.library.util;

import com.library.model.dto.ResourceDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;
import java.util.Map;

public class HttpClientUtil {

    private static final String BASE_URL = "http://localhost:8080/api";
    private static final HttpClient client = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public static String borrowResource(String borrowerId, String resourceId)
            throws Exception {
        String json = mapper.writeValueAsString(
                Map.of("borrowerId", borrowerId,
                        "resourceId", resourceId));
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/borrow/borrow"
                        + "?borrowerId=" + borrowerId
                        + "&resourceId=" + resourceId))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpResponse<String> response = client.send(
                request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException(parseError(response.body()));
        }
        return response.body();
    }

    public static void returnResource(String resourceId) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/borrow/return"
                        + "?resourceId=" + resourceId))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = client.send(
                request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 200) {
            throw new RuntimeException(parseError(response.body()));
        }
    }

    public static List<ResourceDTO> searchResources(String keyword, String type)
            throws Exception {
        String url = BASE_URL + "/resources";
        if (keyword != null && !keyword.isEmpty()) {
            url = BASE_URL + "/resources/search?keyword="
                    + java.net.URLEncoder.encode(keyword, "UTF-8");
        } else if (!"全部".equals(type)) {
            url = BASE_URL + "/resources/type/" + type;
        }
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url)).GET().build();
        HttpResponse<String> response = client.send(
                request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(),
                new TypeReference<List<ResourceDTO>>() {});
    }

    public static List<ResourceDTO> getAllResources() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/resources")).GET().build();
        HttpResponse<String> response = client.send(
                request, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(response.body(),
                new TypeReference<List<ResourceDTO>>() {});
    }

    private static String parseError(String body) {
        try {
            Map<String, String> error = mapper.readValue(body,
                    new TypeReference<Map<String, String>>() {});
            return error.getOrDefault("message", body);
        } catch (Exception e) {
            return body;
        }
    }
}