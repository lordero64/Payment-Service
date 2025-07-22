package com.iprody.payment.service.app.controller;

import com.iprody.payment.service.app.model.Payment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final Payment payment = new Payment(1L, 99.99);
    private final Map<Long, Payment> payments = fillPayments();

    @GetMapping
    public Map<Long, Payment> getPayments() {
        return payments;
    }

    @GetMapping("/{id}")
    public Payment getPayment(@PathVariable("id") Long id) {
        return payments.get(id) != null ? payments.get(id) : null;
    }

    private Map<Long, Payment> fillPayments() {
        return Map.of(1L, new Payment(1, 90), 2L, new Payment(2, 45), 3L, new Payment(3, 20));
    }
}