package com.fitness.activityservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchange;

    @Value("${spring.rabbitmq.queue.name}")
    private String queue;

    @Bean
    public DirectExchange activityExchange(){
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue activityQueue(){
        return new Queue(queue, true);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange activityExchange, @Value("${spring.rabbitmq.routing.key}") String routingKey) {
        return BindingBuilder.bind(queue).to(activityExchange).with(routingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter converter(){
        return new Jackson2JsonMessageConverter();
    }
}
