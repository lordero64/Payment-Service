package com.iprody.payment.service.app.persistence;

import com.iprody.payment.service.app.persistence.entity.Payment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

public final class PaymentFilterFactory {
    public static final Specification<Payment> EMPTY = (root, query, cb) -> null;

    public static Specification<Payment> fromFilter(PaymentFilter paymentFilter) {
        Specification<Payment> spec = EMPTY;

        if (paymentFilter.getStatus() != null) {
            spec = spec.and(PaymentSpecifications.hasStatus(paymentFilter.getStatus()));
        }

        if (StringUtils.hasText(paymentFilter.getCurrency())) {
            spec = spec.and(PaymentSpecifications.hasCurrency(paymentFilter.getCurrency()));
        }

        if (paymentFilter.getMinAmount() != null && paymentFilter.getMaxAmount() != null) {
            spec = spec.and(PaymentSpecifications.amountBetween(
                    paymentFilter.getMinAmount(), paymentFilter.getMaxAmount()));
        }

        if (paymentFilter.getCreatedAfter() != null && paymentFilter.getCreatedBefore() != null) {
            spec = spec.and(PaymentSpecifications.createdBetween(
                    paymentFilter.getCreatedAfter(), paymentFilter.getCreatedBefore()));
        }

        if (paymentFilter.getCreatedAfter() != null) {
            spec = spec.and((PaymentSpecifications.createdAtAfter(paymentFilter.getCreatedAfter())));
        }

        if (paymentFilter.getCreatedBefore() != null) {
            spec = spec.and((PaymentSpecifications.createdAtBefore(paymentFilter.getCreatedBefore())));
        }

        return spec;
    }
}
