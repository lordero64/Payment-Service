package com.iprody.payment.service.app.services;

import com.iprody.payment.service.app.mapper.PaymentMapper;
import com.iprody.payment.service.app.persistence.PaymentFilter;
import com.iprody.payment.service.app.persistence.PaymentFilterFactory;
import com.iprody.payment.service.app.persistence.PaymentRepository;
import com.iprody.payment.service.app.persistence.entity.Payment;
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

    public PaymentDto create(PaymentDto dto) {
        final Payment entity = paymentMapper.toEntity(dto);
        entity.setGuid(null);
        final Payment saved = paymentRepository.save(entity);
        return paymentMapper.toDto(saved);
    }

    public Page<PaymentDto> search(PaymentFilter paymentFilter, Pageable pageable) {
        return paymentRepository.findAll(PaymentFilterFactory.fromFilter(paymentFilter), pageable)
                .map(paymentMapper::toDto);
    }

    public PaymentDto get(UUID id) {
        return paymentRepository.findById(id)
                .map(paymentMapper::toDto).orElseThrow(() -> new EntityNotFoundException("Платеж не найден: " + id));
    }

    public PaymentDto update(UUID id, PaymentDto dto) {
        if (!paymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Платеж не найден: " + id);
        }

        final Payment updated = paymentMapper.toEntity(dto);
        updated.setGuid(id);
        final Payment saved = paymentRepository.save(updated);
        return paymentMapper.toDto(saved);
    }

    public void updateStatus(UUID id, PaymentStatus status) {
        paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Платёж не найден: " + id));

        paymentRepository.updateStatus(id, status);

    }

    public void updateNote(UUID id, String note) {
        paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Платёж не найден: " + id));

        paymentRepository.updateNote(id, note);
    }

    public void delete(UUID id) {
        if (!paymentRepository.existsById(id)) {
            throw new EntityNotFoundException("Платеж не найден: " + id);
        }
        paymentRepository.deleteById(id);
    }

    public List<PaymentDto> getByStatus(PaymentStatus paymentStatus) {
        return paymentRepository.findByStatus(paymentStatus).stream()
                .map(paymentMapper::toDto).toList();
    }

    public List<PaymentDto> getAll() {
        return paymentRepository.findAll().stream().map(paymentMapper::toDto).toList();

    }
}
