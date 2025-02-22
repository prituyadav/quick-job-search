package com.langdb.langDB.resumeATS;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JobService {

    @Value("${langdb.api.url}")
    private String langDbApiUrl;

    @Value("${langdb.api.key}")
    private String langDbApiKey;

    @Value("${langdb.project.id}")
    private String langDbProjectId;

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public JobService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
        this.restTemplate = new RestTemplate();
    }

    public List<String> parseResume(String resumeText) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4o");

            requestBody.put("temperature", 0.8);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", resumeText + " give all the current openings"));
            requestBody.put("messages", messages);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + langDbApiKey);
            headers.set("X-Project-Id", langDbProjectId);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(langDbApiUrl, entity, String.class);

            return extractJobListings(response.getBody());

        } catch (HttpClientErrorException e) {
            System.err.println("LangDB API Error: " + e.getResponseBodyAsString());
            throw new RuntimeException("LangDB API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            throw new RuntimeException("Unexpected error while processing LangDB API response: " + e.getMessage());
        }
    }


    public List<String> parseResumeWithChatGpt(String resumeText) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4o");

            requestBody.put("temperature", 0.8);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", resumeText + " give all the current openings"));
            requestBody.put("messages", messages);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + langDbApiKey);
            headers.set("X-Project-Id", langDbProjectId);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(langDbApiUrl, entity, String.class);

            return extractJobListings(response.getBody());

        } catch (HttpClientErrorException e) {
            System.err.println("LangDB API Error: " + e.getResponseBodyAsString());
            throw new RuntimeException("LangDB API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            throw new RuntimeException("Unexpected error while processing LangDB API response: " + e.getMessage());
        }
    }


    private List<String> extractJobListings(String jsonResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(jsonResponse);

        List<String> jobOpenings = new ArrayList<>();

        if (root.has("choices") && root.get("choices").isArray()) {
            for (JsonNode choice : root.get("choices")) {
                if (choice.has("message") && choice.get("message").has("content")) {
                    jobOpenings.add(choice.get("message").get("content").asText());
                }
            }
        }

        return jobOpenings;
    }

    public List<String> parseResumeWithDeepseek(String resumeText) {
        try {
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "deepseek/deepseek-chat");

            requestBody.put("temperature", 0.8);

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", resumeText + " give all the current openings"));
            requestBody.put("messages", messages);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Authorization", "Bearer " + langDbApiKey);
            headers.set("X-Project-Id", langDbProjectId);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(langDbApiUrl, entity, String.class);

            return extractJobListings(response.getBody());

        } catch (HttpClientErrorException e) {
            System.err.println("LangDB API Error: " + e.getResponseBodyAsString());
            throw new RuntimeException("LangDB API error: " + e.getStatusCode() + " - " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Unexpected Error: " + e.getMessage());
            throw new RuntimeException("Unexpected error while processing LangDB API response: " + e.getMessage());
        }
    }


}
