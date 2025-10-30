package com.iprody.xpayment.adapter.app.async;

import com.iprody.xpayment.adapter.app.api.CreateChargeRequestDto;
import com.iprody.xpayment.adapter.app.api.CreateChargeResponseDto;
import com.iprody.xpayment.adapter.app.api.XPaymentProviderGateway;
import com.iprody.xpayment.adapter.app.checkstate.PaymentStateCheckRegistrar;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import java.time.Instant;

@Component
public class RequestMessageHandler implements MessageHandler<XPaymentAdapterRequestMessage> {
    private static final Logger logger = LoggerFactory.getLogger(RequestMessageHandler.class);
    private final XPaymentProviderGateway xPaymentProviderGateway;
    private final AsyncSender<XPaymentAdapterResponseMessage> asyncSender;
    private final PaymentStateCheckRegistrar paymentStateCheckRegistrar;

    @Autowired
    public RequestMessageHandler(XPaymentProviderGateway xPaymentProviderGateway, AsyncSender<XPaymentAdapterResponseMessage> asyncSender, PaymentStateCheckRegistrar paymentStateCheckRegistrar) {
        this.xPaymentProviderGateway = xPaymentProviderGateway;
        this.asyncSender = asyncSender;
        this.paymentStateCheckRegistrar = paymentStateCheckRegistrar;
    }
    @Override
    public void handle(XPaymentAdapterRequestMessage message) {

        logger.info("Payment request received paymentGuid - {}, amount - {}, currency - {}", message.getPaymentGuid(), message.getAmount(), message.getCurrency());

        CreateChargeRequestDto createChargeRequestDto = new CreateChargeRequestDto();
        createChargeRequestDto.setAmount(message.getAmount());
        createChargeRequestDto.setCurrency(message.getCurrency());
        createChargeRequestDto.setOrder(message.getPaymentGuid());
        try {
            CreateChargeResponseDto createChargeResponseDto = xPaymentProviderGateway.createCharge(createChargeRequestDto);

            logger.info("Payment request with paymentGuid - {} is sentfor payment processing. Current status - ",
            createChargeResponseDto.getStatus());
            XPaymentAdapterResponseMessage responseMessage = new XPaymentAdapterResponseMessage();
            responseMessage.setPaymentGuid(createChargeResponseDto.getOrder());
            responseMessage.setTransactionRefId(createChargeResponseDto.getId());
            responseMessage.setAmount(createChargeResponseDto.getAmount());
            responseMessage.setCurrency(createChargeResponseDto.getCurrency());
            responseMessage.setStatus(XPaymentAdapterStatus.valueOf(createChargeResponseDto.getStatus()));
            responseMessage.setOccurredAt(Instant.now());

            asyncSender.send(responseMessage);
            paymentStateCheckRegistrar.register(createChargeResponseDto.getId(),
                    createChargeResponseDto.getOrder(),
                    createChargeResponseDto.getAmount(),
                    createChargeResponseDto.getCurrency()

            );
        } catch (RestClientException ex) {
            logger.error("Error in time of sending payment request with paymentGuid - {}", message.getPaymentGuid(), ex);

            XPaymentAdapterResponseMessage responseMessage = new XPaymentAdapterResponseMessage();
            responseMessage.setPaymentGuid(message.getPaymentGuid());
            responseMessage.setAmount(message.getAmount());
            responseMessage.setCurrency(message.getCurrency());
            responseMessage.setStatus(XPaymentAdapterStatus.CANCELED);
            responseMessage.setOccurredAt(Instant.now());

            asyncSender.send(responseMessage);
        }
    }
}