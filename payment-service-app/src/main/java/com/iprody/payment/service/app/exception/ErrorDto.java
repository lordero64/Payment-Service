package com.iprody.payment.service.app.exception;

import java.time.Instant;

public class ErrorDto {

    private final String message;
    private final Instant timestamp;
    private final int errorCode;

    public ErrorDto (int errorCode, String message) {
        this.message = message;
        this.timestamp = Instant.now();
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
