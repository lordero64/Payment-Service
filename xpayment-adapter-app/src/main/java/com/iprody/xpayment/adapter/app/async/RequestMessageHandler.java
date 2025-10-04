package com.iprody.xpayment.adapter.app.async;

import com.iprody.xpayment.adapter.app.api.model.ChargeResponse;
import com.iprody.xpayment.adapter.app.api.model.CreateChargeRequest;
import com.iprody.xpayment.adapter.app.api.XPaymentProviderGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.time.Instant;
@Component
public class RequestMessageHandler implements
        MessageHandler<XPaymentAdapterRequestMessage> {
    private static final Logger logger =
            LoggerFactory.getLogger(RequestMessageHandler.class);
    private final XPaymentProviderGateway xPaymentProviderGateway;

    private final AsyncSender<XPaymentAdapterResponseMessage>
            asyncSender;
    @Autowired
    public RequestMessageHandler(
            XPaymentProviderGateway xPaymentProviderGateway,
            AsyncSender<XPaymentAdapterResponseMessage> asyncSender) {
        this.xPaymentProviderGateway = xPaymentProviderGateway;
        this.asyncSender = asyncSender;
    }
    @Override
    public void handle(XPaymentAdapterRequestMessage message) {
        logger.info("Payment request received paymentGuid - {}, amount - {}, currency - {}", message.getPaymentGuid(), message.getAmount(), message.getCurrency());

        CreateChargeRequest createChargeRequest = new CreateChargeRequest();
        createChargeRequest.setAmount(message.getAmount());
        createChargeRequest.setCurrency(message.getCurrency());
        createChargeRequest.setOrder(message.getPaymentGuid());
        try {
            ChargeResponse chargeResponse = xPaymentProviderGateway.createCharge(createChargeRequest);

            logger.info("Payment request with paymentGuid - {} is sentfor payment processing. Current status - ",
            chargeResponse.getStatus());
            XPaymentAdapterResponseMessage responseMessage = new XPaymentAdapterResponseMessage();

            responseMessage.setPaymentGuid(chargeResponse.getOrder());
            responseMessage.setTransactionRefId(chargeResponse.getId());
            responseMessage.setAmount(chargeResponse.getAmount());
            responseMessage.setCurrency(chargeResponse.getCurrency());
            responseMessage.setStatus(XPaymentAdapterStatus.valueOf(chargeResponse.getStatus()));

            responseMessage.setOccurredAt(Instant.now());
            asyncSender.send(responseMessage);
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