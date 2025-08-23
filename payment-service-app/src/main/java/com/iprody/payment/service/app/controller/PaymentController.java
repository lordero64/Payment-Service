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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping()
    @PreAuthorize("hasAnyRole('admin', 'reader')")
    public Page<PaymentDto> search(@ModelAttribute PaymentFilter paymentFilter,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "guid") String sortBy,
        @RequestParam(defaultValue = "desc") String direction) {
        final Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

        final Pageable pageable = PageRequest.of(page, size, sort);
        return paymentService.search(paymentFilter, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('admin')")
    public PaymentDto create(@RequestBody PaymentDto dto) {
        return paymentService.create(dto);
    }

    @GetMapping("/{guid}")
    @PreAuthorize("hasAnyRole('admin', 'reader')")
    public PaymentDto get(@PathVariable UUID guid) {
        return  paymentService.get(guid);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin')")
    public PaymentDto update(@PathVariable UUID id, @RequestBody PaymentDto dto) {
        return paymentService.update(id, dto);
    }

    @PatchMapping("/{id}/status")
    public void updateStatus(
        @PathVariable UUID id,
        @RequestBody @Valid PaymentStatusUpdateDto dto) {
        paymentService.updateStatus(id, dto.getStatus());
    }

    @PatchMapping("/{id}/note")
    public void updateNote(
        @PathVariable UUID id,
        @RequestBody @Valid NoteUpdateDto dto) {
        paymentService.updateNote(id, dto.getNote());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('admin')")
    public void delete(@PathVariable UUID id) {
        paymentService.delete(id);
    }

    @GetMapping("/by_status/{status}")
    public List<PaymentDto> getByStatus(@PathVariable PaymentStatus paymentStatus) {
        return paymentService.getByStatus(paymentStatus);
    }
}