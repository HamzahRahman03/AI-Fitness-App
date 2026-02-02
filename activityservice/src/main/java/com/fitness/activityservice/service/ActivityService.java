package com.fitness.activityservice.service;

import com.fitness.activityservice.dto.ActivityRequest;
import com.fitness.activityservice.dto.ActivityResponse;
import com.fitness.activityservice.model.Activity;
import com.fitness.activityservice.repository.ActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class ActivityService {

    private final ActivityRepository activityRepository;

    public ActivityResponse trackActivity(ActivityRequest request) {

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .startTime(request.getStartTime())
                .duration(request.getDuration())
                .caloriesBurnt(request.getCaloriesBurnt())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepository.save(activity);

        return mapToResponse(savedActivity);
    }

    private ActivityResponse mapToResponse(Activity savedActivity){
        ActivityResponse activityResponse = new ActivityResponse();

        activityResponse.setId(savedActivity.getId());
        activityResponse.setUserId(savedActivity.getUserId());
        activityResponse.setType(savedActivity.getType());
        activityResponse.setStartTime(savedActivity.getStartTime());
        activityResponse.setDuration(savedActivity.getDuration());
        activityResponse.setCaloriesBurnt(savedActivity.getCaloriesBurnt());
        activityResponse.setAdditionalMetrics(savedActivity.getAdditionalMetrics());
        activityResponse.setCreatedAt(savedActivity.getCreatedAt());
        activityResponse.setUpdatedAt(savedActivity.getUpdatedAt());

        return activityResponse;
    }
}
