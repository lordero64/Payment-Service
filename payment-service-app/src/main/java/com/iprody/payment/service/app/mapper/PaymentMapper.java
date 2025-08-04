package com.iprody.payment.service.app.mapper;

import com.iprody.payment.service.app.persistence.entity.Payment;
import com.iprody.payment.service.app.services.PaymentDto;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring",
    unmappedSourcePolicy = ReportingPolicy.ERROR,
    unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface PaymentMapper {
    PaymentDto toDto(Payment payment);
    Payment toEntity(PaymentDto paymentDto);
}
