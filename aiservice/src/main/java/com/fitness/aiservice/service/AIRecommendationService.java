package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIRecommendationService {

    private final GeminiService geminiService;

    public String generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getResponseFromAi(prompt);
        log.info("RESPONSE FROM AI : {}", aiResponse);
        return aiResponse;
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                        Analyse the activity and the metrics of it. Suggest improvements and safety measures.
                        Generate a recommendation for the following activity in JSON format.
                                The JSON must include:
                                {
                                    "overview": "Two-line summary of the current activity",
                                    "recommendation": "Two-line improvement recommendation"
                                }
                                Activity details:
                                {
                                    "type": "%s",
                                    "duration": "%s",
                                    "caloriesBurnt": "%s"
                                }
        Provide only valid JSON, do not include explanations or text outside the JSON object.
        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurnt()
        );
    }
}
