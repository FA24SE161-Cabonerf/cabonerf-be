package com.example.cabonerfbe.config;

import com.rabbitmq.client.Channel;
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

    public static final String CREATE_PROCESS_QUEUE = "process.queue.create";
    public static final String CREATE_PROCESS_EXCHANGE = "process.exchange.create";
    public static final String CREATE_PROCESS_ROUTING_KEY = "process.key.create";

    public static final String CREATED_PROCESS_QUEUE = "process.queue.created";
    public static final String CREATED_PROCESS_EXCHANGE = "process.exchange.created";
    public static final String CREATED_PROCESS_ROUTING_KEY = "process.key.created";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    @Bean
    public Queue createProcessQueue() {
        return new Queue(CREATE_PROCESS_QUEUE);
    }

    @Bean
    public Queue createdProcessQueue() {
        return new Queue(CREATED_PROCESS_QUEUE);
    }

    @Bean
    public DirectExchange createProcessExchange() {
        return new DirectExchange(CREATE_PROCESS_EXCHANGE);
    }

    @Bean
    public DirectExchange createdProcessExchange() {
        return new DirectExchange(CREATED_PROCESS_EXCHANGE);
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
    public Binding createProcessBinding(Queue createProcessQueue, DirectExchange createProcessExchange) {
        return BindingBuilder.bind(createProcessQueue).to(createProcessExchange).with(CREATE_PROCESS_ROUTING_KEY);
    }

    @Bean
    public Binding createdProcessBinding(Queue createdProcessQueue, DirectExchange createdProcessExchange) {
        return BindingBuilder.bind(createdProcessQueue).to(createdProcessExchange).with(CREATED_PROCESS_ROUTING_KEY);
    }

    // RPC queue

    public static final String RPC_QUEUE = "rpc_queue";

    @Bean
    public Queue rpcQueue() {
        return new Queue(RPC_QUEUE);
    }


}
