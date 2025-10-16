package com.iprody.xpayment.adapter.app.mapper;

import com.iprody.xpayment.adapter.app.api.CreateChargeResponseDto;
import com.iprody.xpayment.adapter.app.api.model.ChargeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CreateChargeResponseMapper {
    ChargeResponse toDto(CreateChargeResponseDto CreateChargeResponseDto);

    CreateChargeResponseDto toEntity(ChargeResponse response);
}