package com.iprody.xpayment.adapter.app.checkstate.handler;

import com.iprody.xpayment.adapter.app.api.CreateChargeResponseDto;
import com.iprody.xpayment.adapter.app.api.XPaymentProviderGateway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;

import java.util.UUID;
@Component
public class PaymentCheckStateHandler implements PaymentStatusCheckHandler {
    private final XPaymentProviderGateway xPaymentProviderGateway;
    private static final Logger logger = LoggerFactory.getLogger(PaymentCheckStateHandler.class);
    @Autowired
    public PaymentCheckStateHandler(XPaymentProviderGateway xPaymentProviderGateway) {
        this.xPaymentProviderGateway = xPaymentProviderGateway;
    }

    @Override
    public boolean handle(UUID chargeGuid) {

        try {
            CreateChargeResponseDto createChargeResponseDto = xPaymentProviderGateway.retrieveCharge(chargeGuid);
            final String status = createChargeResponseDto.getStatus();


        }catch (RestClientException ex){
            logger.error("Error with chargeGuid - {}",chargeGuid,  ex);
        }

        //TODO appropriate switch status
        return false;
    }
}
