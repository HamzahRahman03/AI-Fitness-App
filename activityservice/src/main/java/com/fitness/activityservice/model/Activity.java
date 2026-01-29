package com.fitness.activityservice.model;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Map;

@Document()
@Data
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
