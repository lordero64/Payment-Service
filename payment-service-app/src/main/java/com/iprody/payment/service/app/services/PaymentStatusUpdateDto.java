package com.iprody.payment.service.app.services;

import com.iprody.payment.service.app.persistence.entity.PaymentStatus;
import jakarta.validation.constraints.NotNull;

public class PaymentStatusUpdateDto {

    @NotNull
    private PaymentStatus status;
    public PaymentStatus getStatus() {
        return status;
    }
    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
