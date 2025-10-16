package com.iprody.xpayment.adapter.app.mapper;

import com.iprody.xpayment.adapter.app.api.CreateChargeResponseDto;
import com.iprody.xpayment.adapter.app.api.model.ChargeResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface CreateChargeResponseMapper {
    ChargeResponse toDto(CreateChargeResponseDto CreateChargeResponseDto);

    CreateChargeResponseDto toEntity(ChargeResponse response);
}