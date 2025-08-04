package com.iprody.payment.service.app.services;

import com.iprody.payment.service.app.mapper.PaymentMapper;
import com.iprody.payment.service.app.persistence.PaymentFilter;
import com.iprody.payment.service.app.persistence.PaymentFilterFactory;
import com.iprody.payment.service.app.persistence.PaymentRepository;
import com.iprody.payment.service.app.persistence.entity.PaymentStatus;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
    }

    public Page<PaymentDto> findAll(PaymentFilter paymentFilter, Pageable pageable) {
        return paymentRepository.findAll(PaymentFilterFactory.fromFilter(paymentFilter), pageable)
                .map(paymentMapper::toDto);
    }

    public PaymentDto findById(UUID id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toDto).orElseThrow(() -> new EntityNotFoundException("Платеж не найден: " + id));
    }

    public List<PaymentDto> getByStatus(PaymentStatus paymentStatus) {
        return paymentRepository.findByStatus(paymentStatus).stream()
                .map(paymentMapper::toDto).toList();
    }
}
