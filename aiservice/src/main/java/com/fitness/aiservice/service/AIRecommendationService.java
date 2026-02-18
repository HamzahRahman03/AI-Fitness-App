package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
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

        processAIResponse(activity, aiResponse);
        return aiResponse;
    }

    private void processAIResponse(Activity activity, String aiResponse){
        try{
            ObjectMapper objectMapper = new ObjectMapper();

            JsonNode rootNode = objectMapper.readTree(aiResponse);
            JsonNode textNode = rootNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");

            String jsonContent = textNode.asText()
                    .replaceAll("```json\n", "")
                    .replaceAll("```", "")
                    .trim() ;

            log.info("PARSED RESPONSE FROM AI : {}", jsonContent);

        } catch (Exception e){
//            e.printStackTrace();
            log.error("Failed to parse JSON response", e);
        }
    }

    private String createPromptForActivity(Activity activity) {
        return String.format("""
                        For the following activity, analyse the metrics and generate safety measures, recommendation and improvements in JSON format.
                         The JSON must include:
                        {
                                "analysis": {
                                                "overall": "Overall analysis",
                                                "heartRate": "Heart rate analysis",
                                                "caloriesBurned": "Calory analysis"
                                },
                                "improvements": [
                                         {       "area": "area name",
                                                "recommendation": "detailed recommendation"
                                         },
                                ],
                                "suggestions": [
                                        {
                                                "workout": "Workout name",
                                                "description": "Detailed workout description"
                                        }
                                ],
                                "safety":[
                                        "safety point 1",
                                        "safety point 2"
                                ]
                        }
       
        Analyse this activity:
        Activity Type: %s
        Duration: %d minutes
        Calories Burnt: %d
        
        Provide only valid JSON, do not include explanations or text outside the JSON object.
        """,
                activity.getType(),
                activity.getDuration(),
                activity.getCaloriesBurnt()
        );
    }
}
