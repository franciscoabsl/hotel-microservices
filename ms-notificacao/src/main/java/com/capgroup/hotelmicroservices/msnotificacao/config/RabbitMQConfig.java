package com.capgroup.hotelmicroservices.msnotificacao.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Nomes base para a Exchange e a Fila
    public static final String EXCHANGE_RESERVA = "reserva.topic";
    public static final String QUEUE_NOTIFICACAO = "reserva.notificacao.queue";

    // --- Configuração RabbitMQ (Ouvinte) ---

    @Bean
    public TopicExchange reservaTopicExchange() {
        return new TopicExchange(EXCHANGE_RESERVA);
    }

    @Bean
    public Queue notificacaoQueue() {
        // durable: true garante que a fila persista após o restart do RabbitMQ
        return new Queue(QUEUE_NOTIFICACAO, true);
    }

    @Bean
    public Binding bindingNotificacaoQueue(Queue notificacaoQueue, TopicExchange reservaTopicExchange) {
        // Faz o binding: A fila OUVE todos os eventos que comecem com 'reserva.'
        // O ms-reserva publica com chaves como 'reserva.CRIACAO', 'reserva.ALTERACAO', etc.
        return BindingBuilder.bind(notificacaoQueue)
                .to(reservaTopicExchange)
                .with("reserva.#"); // Ouve todos os tipos de eventos de reserva
    }
}
