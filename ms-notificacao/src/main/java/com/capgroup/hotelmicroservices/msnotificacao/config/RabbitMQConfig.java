package com.capgroup.hotelmicroservices.msnotificacao.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String EXCHANGE_RESERVA = "reserva.topic";
    public static final String QUEUE_NOTIFICACAO = "reserva.notificacao.queue";

    @Bean
    public TopicExchange reservaTopicExchange() {
        return new TopicExchange(EXCHANGE_RESERVA);
    }

    @Bean
    public Queue notificacaoQueue() {
        return new Queue(QUEUE_NOTIFICACAO, true);
    }

    @Bean
    public Binding bindingNotificacaoQueue(Queue notificacaoQueue, TopicExchange reservaTopicExchange) {
        return BindingBuilder.bind(notificacaoQueue)
                .to(reservaTopicExchange)
                .with("reserva.#"); // Ouve todos os tipos de eventos de reserva
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
