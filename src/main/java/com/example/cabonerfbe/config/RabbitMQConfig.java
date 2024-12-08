package com.example.cabonerfbe.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {
    public static final String QUEUE = "java.queue";
    public static final String EXCHANGE = "java.exchange";
    public static final String ROUTING_KEY = "java.key";
    // process queue
    public static final String CREATE_PROCESS_QUEUE = "process.queue.create";
    public static final String CREATE_PROCESS_EXCHANGE = "process.exchange.create";
    public static final String CREATE_PROCESS_ROUTING_KEY = "process.key.create";
    public static final String CREATED_PROCESS_QUEUE = "process.queue.created";
    public static final String CREATED_PROCESS_EXCHANGE = "process.exchange.created";
    public static final String CREATED_PROCESS_ROUTING_KEY = "process.key.created";
    // connector queue
    public static final String CONNECTOR_QUEUE = "queue.connector";
    public static final String CONNECTOR_ROUTING_KEY = "key.connector";
    public static final String CONNECTOR_EXCHANGE = "exchange.connector";
    // send email queue
    public static final String EMAIL_QUEUE = "email.queue";
    public static final String EMAIL_EXCHANGE = "email.exchange";
    public static final String EMAIL_CREATE_ORGANIZATION_ROUTING_KEY = "email.create.organization.key";
    public static final String EMAIL_CREATE_ACCOUNT_MANAGER_ROUTING_KEY = "email.create.account.manager.key";
    public static final String EMAIL_REGISTER_ROUTING_KEY = "email.register.key";


    // RPC queue
    public static final String RPC_QUEUE = "rpc_queue";
    public static final String RPC_QUEUE_CONNECTOR = "rpc_queue_connector";

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
    public Queue connectorQueue() {
        return new Queue(CONNECTOR_QUEUE);
    }

    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
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
    public DirectExchange connectorExchange() {
        return new DirectExchange(CONNECTOR_EXCHANGE);
    }

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
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

    @Bean
    public Binding connectorBinding(Queue connectorQueue, DirectExchange connectorExchange) {
        return BindingBuilder.bind(connectorQueue).to(connectorExchange).with(CONNECTOR_ROUTING_KEY);
    }

    @Bean
    public Binding bindingCreateOrganization() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_CREATE_ORGANIZATION_ROUTING_KEY);
    }

    @Bean
    public Binding bindingCreateAccountManager() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_CREATE_ACCOUNT_MANAGER_ROUTING_KEY);
    }

    @Bean
    public Binding bindingRegister() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_REGISTER_ROUTING_KEY);
    }

    @Bean
    public Queue rpcQueue() {
        return new Queue(RPC_QUEUE);
    }

    @Bean
    public Queue rpcQueueConnector() {
        return new Queue(RPC_QUEUE_CONNECTOR);
    }


}
