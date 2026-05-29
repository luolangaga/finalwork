package com.library.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@org.springframework.context.annotation.Profile("prod")
public class RabbitMQConfig {

    public static final String LIBRARY_EXCHANGE = "library.event";
    public static final String BORROW_CREATED_QUEUE = "borrow.created";
    public static final String BORROW_RETURNED_QUEUE = "borrow.returned";
    public static final String RESOURCE_CHANGED_QUEUE = "resource.changed";
    public static final String NOTIFICATION_SEND_QUEUE = "notification.send";

    @Bean
    public TopicExchange libraryExchange() {
        return new TopicExchange(LIBRARY_EXCHANGE, true, false);
    }

    @Bean
    public Queue borrowCreatedQueue() {
        return QueueBuilder.durable(BORROW_CREATED_QUEUE)
                .withArgument("x-dead-letter-exchange", LIBRARY_EXCHANGE + ".dlx")
                .build();
    }

    @Bean
    public Queue borrowReturnedQueue() {
        return QueueBuilder.durable(BORROW_RETURNED_QUEUE)
                .withArgument("x-dead-letter-exchange", LIBRARY_EXCHANGE + ".dlx")
                .build();
    }

    @Bean
    public Queue resourceChangedQueue() {
        return QueueBuilder.durable(RESOURCE_CHANGED_QUEUE)
                .withArgument("x-dead-letter-exchange", LIBRARY_EXCHANGE + ".dlx")
                .build();
    }

    @Bean
    public Queue notificationSendQueue() {
        return QueueBuilder.durable(NOTIFICATION_SEND_QUEUE)
                .withArgument("x-dead-letter-exchange", LIBRARY_EXCHANGE + ".dlx")
                .build();
    }

    @Bean
    public Binding borrowCreatedBinding() {
        return BindingBuilder.bind(borrowCreatedQueue())
                .to(libraryExchange()).with("borrow.#");
    }

    @Bean
    public Binding borrowReturnedBinding() {
        return BindingBuilder.bind(borrowReturnedQueue())
                .to(libraryExchange()).with("return.#");
    }

    @Bean
    public Binding resourceChangedBinding() {
        return BindingBuilder.bind(resourceChangedQueue())
                .to(libraryExchange()).with("resource.#");
    }

    @Bean
    public Binding notificationSendBinding() {
        return BindingBuilder.bind(notificationSendQueue())
                .to(libraryExchange()).with("notification.#");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                System.err.println("消息投递失败: " + cause);
            }
        });
        return template;
    }
}