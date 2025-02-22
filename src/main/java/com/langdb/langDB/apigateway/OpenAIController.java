package com.langdb.langDB.apigateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenAIController {

    @Autowired
    private final OpenAIService openAIService;

    public OpenAIController(OpenAIService openAIService) {
        this.openAIService = openAIService;
    }

    @GetMapping("/apple-earnings")
    public String getAppleEarnings() {
        return openAIService.getAppleEarnings();
    }

    @PostMapping("/text")
    public String chat(@RequestBody ChatRequest chatRequest) {
        return openAIService.chatWithOpenAIRouting(chatRequest.getMessage());
    }
}
