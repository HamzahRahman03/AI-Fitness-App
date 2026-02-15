package com.fitness.aiservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class GeminiService {

    @Value("${gemini.api.url}")
    private String geminiAPIURL;

    @Value("${gemini.api.key}")
    private String geminiAPIKey;

    private final WebClient geminiWebClient;

    public GeminiService(WebClient.Builder webClientBuilder){
        this.geminiWebClient = webClientBuilder
                .build();
    }

    public String getResponseFromAi(String prompt){
        Map<String, Object[]> requestBody = Map.of(
                "contents", new Object[]{
                        Map.of(
                                "parts", new Object[]{
                                        Map.of(
                                                "text", prompt
                                        )
                                }
                        )
                }
        );

        String response = geminiWebClient.post()
                .uri(geminiAPIURL+geminiAPIKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return response;
    }
}
