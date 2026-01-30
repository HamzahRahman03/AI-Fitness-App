package com.fitness.activityservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document(collection = "activities")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Activity {
    private String id;
    private String userId;
    private LocalDateTime startTime;
    private Integer duration;
    private Integer caloriesBurnt;
    private ActivityType type;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Map<String, Object> additionalMetrics;
}
