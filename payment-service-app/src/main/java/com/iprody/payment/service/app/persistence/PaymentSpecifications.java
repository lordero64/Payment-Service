package com.iprody.payment.service.app.persistence;

import com.iprody.payment.service.app.persistence.entity.Payment;
import com.iprody.payment.service.app.persistence.entity.PaymentStatus;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.Instant;

public final class PaymentSpecifications {

    public static Specification<Payment> hasStatus(PaymentStatus paymentStatus){
        return (root, query, cb) -> cb.equal(root.get("status"), paymentStatus);
    }

    public static Specification<Payment> createdAtAfter(Instant after){
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), after);
    }

    public static Specification<Payment> createdAtBefore(Instant before){
        return (root, query, cb) -> cb.lessThan(root.get("createdAt"), before);
    }

    public static Specification<Payment> hasCurrency(String currency) {
        return (root, query, cb) -> cb.equal(root.get("currency"),
                currency);
    }
    public static Specification<Payment> amountBetween(BigDecimal min,
                                                       BigDecimal max) {
        return (root, query, cb) -> cb.between(root.get("amount"), min,
                max);
    }
    public static Specification<Payment> createdBetween(Instant after,
                                                        Instant before) {
        return (root, query, cb) -> cb.between(root.get("createdAt"),
                after, before);
    }
}
