package com.iprody.payment.service.app.controller;

import com.iprody.payment.service.app.persistence.PaymentFilter;
import com.iprody.payment.service.app.persistence.entity.PaymentStatus;
import com.iprody.payment.service.app.services.NoteUpdateDto;
import com.iprody.payment.service.app.services.PaymentDto;
import com.iprody.payment.service.app.services.PaymentService;
import com.iprody.payment.service.app.services.PaymentStatusUpdateDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping()
//    @PreAuthorize("hasAnyRole('admin', 'reader', 'user')")
    public Page<PaymentDto> search(@ModelAttribute PaymentFilter paymentFilter,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "guid") String sortBy,
        @RequestParam(defaultValue = "desc") String direction) {
        final Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

        log.info("GET payments by filter: {}", paymentFilter.toString());
        final Pageable pageable = PageRequest.of(page, size, sort);

        final Page<PaymentDto> payments = paymentService.search(paymentFilter, pageable);
        log.debug("Sending response Payments: {}", payments);
        return payments;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
//    @PreAuthorize("hasRole('admin')")
    public PaymentDto create(@RequestBody PaymentDto dto) {
        log.info("POST payment: {}", dto.getGuid());
        final PaymentDto newDto = paymentService.create(dto);
        log.debug("Sending response PaymentDto: {}", newDto);
        return newDto;
    }

    @GetMapping("/{guid}")
//    @PreAuthorize("hasAnyRole('admin', 'reader', 'user')")
    public PaymentDto get(@PathVariable UUID guid) {
        log.info("GET payment by id: {}", guid);
        final PaymentDto dto = paymentService.get(guid);
        log.debug("Sending response PaymentDto: {}", dto);
        return dto;
    }

    @PutMapping("/{id}")
//    @PreAuthorize("hasAnyRole('admin')")
    public PaymentDto update(@PathVariable UUID id, @RequestBody PaymentDto dto) {
        log.info("PUT payment by id: {}", id);
        log.debug("update payment: {}", dto);
        return paymentService.update(id, dto);
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(
        @PathVariable UUID id,
        @RequestBody @Valid PaymentStatusUpdateDto dto) {
        log.info("PATCH payment by id: {}", id);
        log.debug("update payment status: {}", dto.getStatus());
        paymentService.updateStatus(id, dto.getStatus());
    }

    @PatchMapping("/{id}/note")
    public void updateNote(
        @PathVariable UUID id,
        @RequestBody @Valid NoteUpdateDto dto) {
        log.info("PATCH payment by id: {}", id);
        log.debug("update payment note: {}", dto.getNote());
        paymentService.updateNote(id, dto.getNote());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
//    @PreAuthorize("hasRole('admin')")
    public void delete(@PathVariable UUID id) {
        log.info("DELETE payment by id: {}", id);
        paymentService.delete(id);
    }

    @GetMapping("/by_status/{status}")
    public List<PaymentDto> getByStatus(@PathVariable PaymentStatus paymentStatus) {
        log.info("GET payments by status: {}", paymentStatus);
        return paymentService.getByStatus(paymentStatus);
    }
}