package com.example.cabonerfbe.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    // convert to json format
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    public static final String QUEUE = "java.queue";
    public static final String EXCHANGE = "java.exchange";
    public static final String ROUTING_KEY = "java.key";

    public static final String CREATE_PROCESS_QUEUE = "create.process.queue";
    public static final String CREATE_PROCESS_EXCHANGE = "create.process.exchange";
    public static final String CREATE_PROCESS_ROUTING_KEY = "create.process.key";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public Queue createProcessQueue() {
        return new Queue(CREATE_PROCESS_QUEUE);
    }

    @Bean
    public DirectExchange createProcessExchange() {
        return new DirectExchange(CREATE_PROCESS_EXCHANGE);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public Binding processBinding(Queue createProcessQueue, DirectExchange createProcessExchange) {
        return BindingBuilder.bind(createProcessQueue).to(createProcessExchange).with(CREATE_PROCESS_ROUTING_KEY);
    }


}
