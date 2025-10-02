package com.iprody.xpayment.adapter.app.async.kafka;

import com.iprody.xpayment.adapter.app.async.AsyncListener;
import com.iprody.xpayment.adapter.app.async.MessageHandler;
import com.iprody.xpayment.adapter.app.async.XPaymentAdapterRequestMessage;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
public class KafkaXPaymentAdapterRequestListenerAdapter implements AsyncListener<XPaymentAdapterRequestMessage> {
    private static final Logger log = LoggerFactory.getLogger(KafkaXPaymentAdapterRequestListenerAdapter.class);

    private final MessageHandler<XPaymentAdapterRequestMessage> handler;
    public KafkaXPaymentAdapterRequestListenerAdapter(MessageHandler<XPaymentAdapterRequestMessage> handler) {
        this.handler = handler;
    }

    @Override
    public void onMessage(XPaymentAdapterRequestMessage message) {
        handler.handle(message);
    }

    @KafkaListener(topics = "${app.kafka.topics.x-payment-adapter.request:xpayment-adapter.requests}", groupId = "${spring.kafka.consumer.group-id}")
    public void consume(XPaymentAdapterRequestMessage message,
            ConsumerRecord<String, XPaymentAdapterRequestMessage> record,
            Acknowledgment ack
    ) {
        try {
            log.info("Received XPayment Adapter request: paymentGuid={}, partition={}, offset={}", message.getPaymentGuid(), record.partition(), record.offset());

            onMessage(message);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Error handling XPayment Adapter request for paymentGuid={}", message.getPaymentGuid(), e);

            throw e;
        }
    }
}
