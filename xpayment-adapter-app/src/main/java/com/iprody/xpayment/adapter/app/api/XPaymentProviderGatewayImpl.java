package com.iprody.xpayment.adapter.app.api;

import com.iprody.xpayment.adapter.app.api.client.DefaultApi;
import com.iprody.xpayment.adapter.app.api.model.ChargeResponse;

import com.iprody.xpayment.adapter.app.mapper.CreateChargeRequestMapper;
import com.iprody.xpayment.adapter.app.mapper.CreateChargeResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import java.util.UUID;

@Service
class XPaymentProviderGatewayImpl implements XPaymentProviderGateway {
    private final DefaultApi defaultApi;
    private final CreateChargeResponseMapper responseMapper;
    private final CreateChargeRequestMapper requestMapper;

    @Autowired
    public XPaymentProviderGatewayImpl(DefaultApi defaultApi, CreateChargeResponseMapper responseMapper, CreateChargeRequestMapper requestMapper) {
        this.defaultApi = defaultApi;
        this.responseMapper = responseMapper;
        this.requestMapper = requestMapper;
    }

    @Override
    public CreateChargeResponseDto createCharge(CreateChargeRequestDto createChargeRequestDto) throws RestClientException {
        return responseMapper.toEntity( defaultApi.createCharge(requestMapper.toEntity(createChargeRequestDto)));
    }

    @Override
    public ChargeResponse retrieveCharge(UUID id) throws RestClientException {
        return null;
    }
}