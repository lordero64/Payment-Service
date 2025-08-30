package com.iprody.payment.service.app.exception;

import java.util.UUID;

public class NotFoundException extends RuntimeException {
    public NotFoundException(Class<?> entity, UUID id) {
        super("Платёж не найден: " + id);
    }
}
