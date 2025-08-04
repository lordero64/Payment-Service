package com.iprody.payment.service.app.controller;

import com.iprody.payment.service.app.persistence.PaymentFilter;
import com.iprody.payment.service.app.persistence.entity.PaymentStatus;
import com.iprody.payment.service.app.services.PaymentDto;
import com.iprody.payment.service.app.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/all")
    public Page<PaymentDto> getAll(@ModelAttribute PaymentFilter paymentFilter,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size,
        @RequestParam(defaultValue = "guid") String sortBy,
        @RequestParam(defaultValue = "desc") String direction) {
        final Sort sort = direction.equalsIgnoreCase("desc")
            ? Sort.by(sortBy).descending()
            : Sort.by(sortBy).ascending();

        final Pageable pageable = PageRequest.of(page, size, sort);
        return paymentService.findAll(paymentFilter, pageable);
    }

    @GetMapping("/{guid}")
    public PaymentDto getById(@PathVariable UUID guid) {
        return  paymentService.findById(guid);
    }

    @GetMapping("/by_status/{status}")
    public List<PaymentDto> getByStatus(@PathVariable PaymentStatus paymentStatus) {
        return paymentService.getByStatus(paymentStatus);
    }


}