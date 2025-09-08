package com.iprody.payment.service.app.async;

import com.iprody.payment.service.app.exception.NotFoundException;
import com.iprody.payment.service.app.mapper.PaymentMapper;
import com.iprody.payment.service.app.persistence.PaymentRepository;
import com.iprody.payment.service.app.persistence.entity.Payment;
import com.iprody.payment.service.app.persistence.entity.PaymentStatus;
import com.iprody.payment.service.app.services.PaymentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PaymentMessageHandler implements MessageHandler<XPaymentAdapterResponseMessage> {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private static final Logger logger = LoggerFactory.getLogger(PaymentMessageHandler.class);

    @Autowired
    public PaymentMessageHandler(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    @Override
    public void handle(XPaymentAdapterResponseMessage message) {

        logger.info("New response message with id {} and status {}", message.getMessageId(), message.getStatus());
        final PaymentDto payment = paymentRepository.findById(message.getPaymentGuid())
            .map(paymentMapper::toDto).orElseThrow(() -> new NotFoundException(Payment.class, message.getMessageId()));

        if (message.getStatus() == XPaymentAdapterStatus.SUCCEEDED) {
            payment.setStatus(PaymentStatus.APPROVED);
        } else {
            payment.setStatus(PaymentStatus.DECLINED);
        }

        paymentRepository.save(paymentMapper.toEntity(payment));
    }
}
