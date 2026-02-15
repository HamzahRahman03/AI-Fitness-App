package com.fitness.activityservice.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public DirectExchange activityExchange(@Value("${spring.rabbitmq.exchange.name}") String exchange){
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue activityQueue(@Value("${spring.rabbitmq.queue.name}") String queue){
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
