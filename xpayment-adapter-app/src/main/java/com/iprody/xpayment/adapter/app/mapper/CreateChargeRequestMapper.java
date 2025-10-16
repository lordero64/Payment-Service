package com.iprody.xpayment.adapter.app.mapper;

import com.iprody.xpayment.adapter.app.api.CreateChargeRequestDto;
import com.iprody.xpayment.adapter.app.api.model.CreateChargeRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
        unmappedSourcePolicy = ReportingPolicy.ERROR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface CreateChargeRequestMapper {
    CreateChargeRequest toEntity(CreateChargeRequestDto createChargeRequestDto);
    CreateChargeRequestDto toDto(CreateChargeRequest createChargeRequest);
}
