package com.iprody.xpayment.adapter.app.checkstate;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqPaymentRetryConfig {
    @Value("${spring.app.rabbitmq.queue-name")
    private String queueName;
    @Value("${spring.app.rabbitmq.exchange-name}")
    private String exchangeName;
    @Value("${spring.app.rabbitmq.dlx-exchange-name}")
    private String dlxExchangeName;
    @Value("${spring.app.rabbitmq.dlx-routing-key}")
    private String dlxRoutingKey;

    @Bean
    public MessageConverter jackson2MessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue xpaymentQueue() {
        return QueueBuilder.durable(queueName)
                .withArgument("x-dead-letter-exchange", dlxExchangeName)
                .withArgument("x-dead-letter-routing-key", dlxRoutingKey)
                .withArgument("x-message-ttl", 86400000) // 24 hours
                .build();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public CustomExchange delayedExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(exchangeName, "x-delayed-message", true, false, args);
    }

    @Bean
    public Binding queueBinding(Queue xpaymentQueue, CustomExchange delayedExchange) {
        return BindingBuilder.bind(xpaymentQueue).to(delayedExchange).with(queueName).noargs();
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

}
