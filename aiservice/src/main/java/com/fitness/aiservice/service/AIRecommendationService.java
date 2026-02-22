package com.fitness.aiservice.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.aiservice.model.Activity;
import com.fitness.aiservice.model.Recommendation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AIRecommendationService {

    private final GeminiService geminiService;

    public Recommendation generateRecommendation(Activity activity){
        String prompt = createPromptForActivity(activity);
        String aiResponse = geminiService.getResponseFromAi(prompt);
//        String aiResponse = "";   //default recommendation generation
        return processAIResponse(activity, aiResponse);
    }

    private Recommendation processAIResponse(Activity activity, String aiResponse){
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

            JsonNode parsedJsonContent = objectMapper.readTree(jsonContent);
            JsonNode analysisNode = parsedJsonContent.path("analysis");


            StringBuilder analysisStringBuilder = new StringBuilder();
            createAnalysisString(analysisNode, analysisStringBuilder, "overall", "Overall: ");
            createAnalysisString(analysisNode, analysisStringBuilder, "heartRate", "Heart Rate: ");
            createAnalysisString(analysisNode, analysisStringBuilder, "caloriesBurnt", "Calories Burnt: ");

            List<String> improvementsList = extractImprovements(parsedJsonContent.path("improvements"));
            List<String> suggestionsList = extractSuggestions(parsedJsonContent.path("suggestions"));
            List<String> safetyMeasuresList = extractSafetyMeasures(parsedJsonContent.path("safety measures"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .activityType(activity.getType())
                    .recommendation(analysisStringBuilder.toString().trim())
                    .createdAt(String.valueOf(LocalDateTime.now()))
                    .improvements(improvementsList)
                    .suggestions(suggestionsList)
                    .safetyMeasures(safetyMeasuresList)
                    .build();

        } catch (Exception e){
            log.error("Failed to parse JSON response", e);
            return generateDefaultRecommendation(activity);
        }
    }

    private Recommendation generateDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .activityType(activity.getType())
                .recommendation("Failed to generate detailed analysis.")
                .createdAt(String.valueOf(LocalDateTime.now()))
                .improvements(Collections.singletonList("Continue with your general warm up routine."))
                .suggestions(Collections.singletonList("Do not forget to warm up and stretch properly before exercises."))
                .safetyMeasures(Collections.singletonList("Stay hydrated and always listen to your body."))
                .build();
    }

    private void createAnalysisString(JsonNode analysisNode, StringBuilder analysisStringBuilder, String key, String prefix ) {
        if(!analysisNode.path(key).isMissingNode()){
            analysisStringBuilder.append(prefix)
                    .append(analysisNode.path(key))
                    .append("\n");
        }
    }

    private List<String> extractImprovements(JsonNode improvementsNode) {
        List<String> improvements = new ArrayList<>();

        if(improvementsNode.isArray()){
            improvementsNode.forEach(improvement -> {
                String area = improvement.path("area").asText();
                String detail = improvement.path("recommendation").asText();
                improvements.add(String.format("%s: %s", area, detail));
            });
        }
        return improvements.isEmpty() ? Collections.singletonList("No specific  improvements provided.") : improvements;
    }

    private List<String> extractSuggestions(JsonNode suggestionsNode) {
        List<String> suggestionsList = new ArrayList<>();

        if(suggestionsNode.isArray()){
            suggestionsNode.forEach(suggestion -> {
                String workout = suggestion.path("workout").asText();
                        String description = suggestion.path("description").asText();
                        suggestionsList.add(String.format("%s: %s", workout, description));
            });
        }

        return suggestionsList.isEmpty() ? Collections.singletonList("No specific suggestions provided.") : suggestionsList;
    }

    private List<String> extractSafetyMeasures(JsonNode safetyMeasuresNode) {
        List<String> safetyMeasures = new ArrayList<>();

        if(safetyMeasuresNode.isArray()){
            safetyMeasuresNode.forEach(safetyMeasure ->
                safetyMeasures.add(safetyMeasure.asText()));
        }

        return safetyMeasures.isEmpty() ? Collections.singletonList("Follow general safety guidelines") : safetyMeasures;
    }



    private String createPromptForActivity(Activity activity) {
        return String.format("""
                        For the following activity, analyse the metrics and generate safety measures, recommendation and improvements in JSON format.
                         The JSON must include:
                        {
                                "analysis": {
                                                "overall": "Overall analysis",
                                                "heartRate": "Heart rate analysis",
                                                "caloriesBurnt": "Calory analysis"
                                },
                                "improvements": [
                                         {      "area": "area name",
                                                "recommendation": "detailed recommendation"
                                         },
                                ],
                                "suggestions": [
                                        {
                                                "workout": "Workout name",
                                                "description": "Detailed workout description"
                                        }
                                ],
                                "safety measures":[
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
