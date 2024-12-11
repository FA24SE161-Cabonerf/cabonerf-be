package com.example.cabonerfbe.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * The class Rabbit mq config.
 *
 * @author SonPHH.
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {
    /**
     * The constant QUEUE.
     */
    public static final String QUEUE = "java.queue";
    /**
     * The constant EXCHANGE.
     */
    public static final String EXCHANGE = "java.exchange";
    /**
     * The constant ROUTING_KEY.
     */
    public static final String ROUTING_KEY = "java.key";
    /**
     * The constant CREATE_PROCESS_QUEUE.
     */
// process queue
    public static final String CREATE_PROCESS_QUEUE = "process.queue.create";
    /**
     * The constant CREATE_PROCESS_EXCHANGE.
     */
    public static final String CREATE_PROCESS_EXCHANGE = "process.exchange.create";
    /**
     * The constant CREATE_PROCESS_ROUTING_KEY.
     */
    public static final String CREATE_PROCESS_ROUTING_KEY = "process.key.create";
    /**
     * The constant CREATED_PROCESS_QUEUE.
     */
    public static final String CREATED_PROCESS_QUEUE = "process.queue.created";
    /**
     * The constant CREATED_PROCESS_EXCHANGE.
     */
    public static final String CREATED_PROCESS_EXCHANGE = "process.exchange.created";
    /**
     * The constant CREATED_PROCESS_ROUTING_KEY.
     */
    public static final String CREATED_PROCESS_ROUTING_KEY = "process.key.created";
    /**
     * The constant CONNECTOR_QUEUE.
     */
// connector queue
    public static final String CONNECTOR_QUEUE = "queue.connector";
    /**
     * The constant CONNECTOR_ROUTING_KEY.
     */
    public static final String CONNECTOR_ROUTING_KEY = "key.connector";
    /**
     * The constant CONNECTOR_EXCHANGE.
     */
    public static final String CONNECTOR_EXCHANGE = "exchange.connector";
    /**
     * The constant EMAIL_QUEUE.
     */
// send email queue
    public static final String EMAIL_QUEUE = "email.queue";
    /**
     * The constant EMAIL_EXCHANGE.
     */
    public static final String EMAIL_EXCHANGE = "email.exchange";
    /**
     * The constant EMAIL_CREATE_ORGANIZATION_ROUTING_KEY.
     */
    public static final String EMAIL_CREATE_ORGANIZATION_ROUTING_KEY = "email.create.organization.key";
    /**
     * The constant EMAIL_CREATE_ACCOUNT_MANAGER_ROUTING_KEY.
     */
    public static final String EMAIL_CREATE_ACCOUNT_MANAGER_ROUTING_KEY = "email.create.account.manager.key";
    /**
     * The constant EMAIL_REGISTER_ROUTING_KEY.
     */
    public static final String EMAIL_REGISTER_ROUTING_KEY = "email.register.key";


    /**
     * The constant RPC_QUEUE.
     */
// RPC queue
    public static final String RPC_QUEUE = "rpc_queue";
    /**
     * The constant RPC_QUEUE_CONNECTOR.
     */
    public static final String RPC_QUEUE_CONNECTOR = "rpc_queue_connector";

    /**
     * Json message converter method.
     *
     * @return the message converter
     */
// convert to json format
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * Template method.
     *
     * @param connectionFactory the connection factory
     * @return the amqp template
     */
    @Bean
    public AmqpTemplate template(ConnectionFactory connectionFactory) {
        final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    /**
     * Queue method.
     *
     * @return the queue
     */
    @Bean
    public Queue queue() {
        return new Queue(QUEUE);
    }

    /**
     * Create process queue method.
     *
     * @return the queue
     */
    @Bean
    public Queue createProcessQueue() {
        return new Queue(CREATE_PROCESS_QUEUE);
    }

    /**
     * Created process queue method.
     *
     * @return the queue
     */
    @Bean
    public Queue createdProcessQueue() {
        return new Queue(CREATED_PROCESS_QUEUE);
    }

    /**
     * Connector queue method.
     *
     * @return the queue
     */
    @Bean
    public Queue connectorQueue() {
        return new Queue(CONNECTOR_QUEUE);
    }

    /**
     * Email queue method.
     *
     * @return the queue
     */
    @Bean
    public Queue emailQueue() {
        return new Queue(EMAIL_QUEUE, true);
    }

    /**
     * Create process exchange method.
     *
     * @return the direct exchange
     */
    @Bean
    public DirectExchange createProcessExchange() {
        return new DirectExchange(CREATE_PROCESS_EXCHANGE);
    }

    /**
     * Created process exchange method.
     *
     * @return the direct exchange
     */
    @Bean
    public DirectExchange createdProcessExchange() {
        return new DirectExchange(CREATED_PROCESS_EXCHANGE);
    }

    /**
     * Exchange method.
     *
     * @return the direct exchange
     */
    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(EXCHANGE);
    }

    /**
     * Connector exchange method.
     *
     * @return the direct exchange
     */
    @Bean
    public DirectExchange connectorExchange() {
        return new DirectExchange(CONNECTOR_EXCHANGE);
    }

    /**
     * Email exchange method.
     *
     * @return the direct exchange
     */
    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    /**
     * Binding method.
     *
     * @param queue    the queue
     * @param exchange the exchange
     * @return the binding
     */
    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    /**
     * Create process binding method.
     *
     * @param createProcessQueue    the create process queue
     * @param createProcessExchange the create process exchange
     * @return the binding
     */
    @Bean
    public Binding createProcessBinding(Queue createProcessQueue, DirectExchange createProcessExchange) {
        return BindingBuilder.bind(createProcessQueue).to(createProcessExchange).with(CREATE_PROCESS_ROUTING_KEY);
    }

    /**
     * Created process binding method.
     *
     * @param createdProcessQueue    the created process queue
     * @param createdProcessExchange the created process exchange
     * @return the binding
     */
    @Bean
    public Binding createdProcessBinding(Queue createdProcessQueue, DirectExchange createdProcessExchange) {
        return BindingBuilder.bind(createdProcessQueue).to(createdProcessExchange).with(CREATED_PROCESS_ROUTING_KEY);
    }

    /**
     * Connector binding method.
     *
     * @param connectorQueue    the connector queue
     * @param connectorExchange the connector exchange
     * @return the binding
     */
    @Bean
    public Binding connectorBinding(Queue connectorQueue, DirectExchange connectorExchange) {
        return BindingBuilder.bind(connectorQueue).to(connectorExchange).with(CONNECTOR_ROUTING_KEY);
    }

    /**
     * Binding create organization method.
     *
     * @return the binding
     */
    @Bean
    public Binding bindingCreateOrganization() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_CREATE_ORGANIZATION_ROUTING_KEY);
    }

    /**
     * Binding create account manager method.
     *
     * @return the binding
     */
    @Bean
    public Binding bindingCreateAccountManager() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_CREATE_ACCOUNT_MANAGER_ROUTING_KEY);
    }

    /**
     * Binding register method.
     *
     * @return the binding
     */
    @Bean
    public Binding bindingRegister() {
        return BindingBuilder.bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_REGISTER_ROUTING_KEY);
    }

    /**
     * Rpc queue method.
     *
     * @return the queue
     */
    @Bean
    public Queue rpcQueue() {
        return new Queue(RPC_QUEUE);
    }

    /**
     * Rpc queue connector method.
     *
     * @return the queue
     */
    @Bean
    public Queue rpcQueueConnector() {
        return new Queue(RPC_QUEUE_CONNECTOR);
    }


}
