package com.fitness.aiservice.service;

import com.fitness.aiservice.model.Activity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ActivityListener {

    @RabbitListener(queues = "activity.queue")
    public void receivedActivity(Activity activity){
        log.info("Received activity from queue : " + activity.getId());
    }
}
