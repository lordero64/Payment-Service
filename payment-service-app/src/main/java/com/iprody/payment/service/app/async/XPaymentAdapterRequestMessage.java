package com.iprody.payment.service.app.async;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
/**
 * Сообщение-запрос для платёжной системы X Payment.
 * <p>
 * Используется для передачи информации о платеже, включая
 идентификаторы,

 * сумму, валюту и время возникновения события.
 * Реализует интерфейс {@link Message}, обеспечивая уникальный
 идентификатор сообщения
 * и метку времени его возникновения.
 */

@Component
public class XPaymentAdapterRequestMessage implements Message {
    /**
     * Уникальный идентификатор платежа.
     */
    private UUID paymentGuid;
    /**
     * Сумма платежа.
     */
    private BigDecimal amount;
    /**
     * Валюта платежа в формате ISO 4217 (например, "USD", "EUR").
     */
    private String currency;
    /**
     * Момент времени, когда событие произошло.
     */
    private Instant occurredAt;
    @Override
    public UUID getMessageId() {
        return paymentGuid;
    }
    public UUID getPaymentGuid() {
        return paymentGuid;
    }
    public void setPaymentGuid(UUID paymentGuid) {
        this.paymentGuid = paymentGuid;
    }
    public BigDecimal getAmount() {
        return amount;
    }
    public void setAmount(BigDecimal amount) {
        this.amount = amount;

    }
    public String getCurrency() {
        return currency;
    }
    public void setCurrency(String currency) {
        this.currency = currency;
    }
    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }
    public void setOccurredAt(Instant occurredAt) {
        this.occurredAt = occurredAt;
    }
}