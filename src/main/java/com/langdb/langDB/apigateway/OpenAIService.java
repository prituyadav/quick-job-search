package com.langdb.langDB.apigateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;


@Service
public class OpenAIService {

    private final RestTemplate restTemplate = new RestTemplate();

    // Read API key from application properties
    @Value("${openai.api.key}") // Ensure to set this property in application.properties
    private String apiKey;

    private static final String BASE_URL =
            "https://api.us-east-1.langdb.ai/b447c835-d42b-47ab-891d-6305878d91b7/v1/chat/completions";
    private ObjectMapper objectMapper;

    public String getAppleEarnings() {
        // Create the request body
        String requestBody = "{"
                + "\"model\": \"openai/gpt-4o-mini\","
                + "\"messages\": ["
                + "{ \"role\": \"system\", \"content\": \"You are a helpful assistant.\" }, "
                + "{ \"role\": \"user\", \"content\": \"What are the earnings of Apple in 2022?\" }"
                + "]"
                + "}";

        // Set up headers including API Key
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send the request and handle the response
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, String.class);

        // Return the response body
        return response.getBody();
    }

    public String chatWithOpenAI(String userMessage) {
        // Create the request body
        String requestBody = String.format("{"
                + "\"model\": \"openai/gpt-4o-mini\","
                + "\"messages\": ["
                + "{ \"role\": \"system\", \"content\": \"You are a helpful assistant.\" }, "
                + "{ \"role\": \"user\", \"content\": \"%s\" }"
                + "]"
                + "}", userMessage);

        // Set up headers including API Key
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send the request and handle the response
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, String.class);

        // Assuming the response will have a format from which we want just the assistant's message
        // You might want to parse JSON to extract the relevant part; here's a simple way to do so:
        return response.getBody();
    }

    public String chatWithOpenAIRouting(String userMessage) {
        // Create a dynamic routing request
        String requestBody = "{"
                + "\"model\": \"router/dynamic\","
                + "\"messages\": ["
                + "{ \"role\": \"system\", \"content\": \"You are a helpful assistant.\" },"
                + "{ \"role\": \"user\", \"content\": \"" + userMessage + "\" }"
                + "],"
                + "\"router\": {"
                + "\"type\": \"fallback\","
                + "\"targets\": ["
                + "{ \"model\": \"openai/gpt-4o-mini\", \"temperature\": 0.7, \"max_tokens\": 300 },"
                + "{ \"model\": \"deepseek/deepseek-chat\", \"temperature\": 0.8, \"max_tokens\": 400 }"
                + "]"
                + "}"
                + "}";

        // Set up headers including API Key
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        // Send the request and handle the response
        ResponseEntity<String> response = restTemplate.exchange(BASE_URL, HttpMethod.POST, requestEntity, String.class);

        // Parse response to extract the chatbot's answer
        return extractChatbotResponse(response.getBody());
    }

    private String extractChatbotResponse(String responseBody) {
        try {
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            return jsonNode.get("choices").get(0).get("message").get("content").asText();
        } catch (IOException e) {
            return "Error processing AI response.";
        }
    }
}
