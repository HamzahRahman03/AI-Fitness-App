package com.fitness.aiservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "ai_recommendations")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {

    @Id
    private String id;
    private String activityId;
    private String userId;
    private String activityType;
    private String recommendation;

    @CreatedDate
    private String createdAt;

    @LastModifiedDate
    private String updatedAt;

    private List<String> improvements;
    private List<String> suggestions;
    private List<String> safetyMeasures;

}
