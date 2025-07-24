package com.iprody.payment.service.app.controller;

import com.iprody.payment.service.app.persistence.PaymentRepository;
import com.iprody.payment.service.app.persistence.entity.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentRepository paymentRepository;

    @GetMapping("/all")
    public List<Payment> getAll() {
        return paymentRepository.findAll();
    }

    @GetMapping("/{guid}")
    public Payment getById(@PathVariable UUID guid) {
        return  paymentRepository.findById(guid).get();
    }
}