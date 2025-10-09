package com.capgroup.hotelmicroservices.msreserva.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${spring.rabbitmq.exchange.reserva}")
    private String reservaExchange;

    @Bean
    public TopicExchange reservaTopicExchange() {
        return new TopicExchange(reservaExchange);
    }
}
