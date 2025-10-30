package com.iprody.xpayment.adapter.app.checkstate;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqDlxConfig {

    @Value("${spring.app.rabbitmq.dlx-exchange-name}")
    private String deadLetterExchangeName;

    @Value("${spring.app.rabbitmq.dlx-routing-key}")
    private String deadLetterRoutingKey;

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(deadLetterExchangeName, true, false);
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable("payments.dead.letter.queue").build();
    }

    @Bean
    public Binding dlxBinding(Queue deadLetterQueue, DirectExchange deadLetterExchange) {
        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(deadLetterRoutingKey);
    }
}