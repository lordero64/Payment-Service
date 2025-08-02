package com.iprody.payment.service.app.controller;

import com.iprody.payment.service.app.persistence.PaymentFilter;
import com.iprody.payment.service.app.persistence.PaymentFilterFactory;
import com.iprody.payment.service.app.persistence.PaymentRepository;
import com.iprody.payment.service.app.persistence.entity.Payment;
import com.iprody.payment.service.app.persistence.entity.PaymentStatus;
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
    private PaymentRepository paymentRepository;

    @GetMapping("/all")
    public Page<Payment> getAll(@ModelAttribute PaymentFilter paymentFilter,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size,
                                @RequestParam(defaultValue = "guid") String sortBy,
                                @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = direction.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        return paymentRepository.findAll(PaymentFilterFactory.fromFilter(paymentFilter), pageable);
    }

    @GetMapping("/{guid}")
    public Payment getById(@PathVariable UUID guid) {
        return  paymentRepository.findById(guid).get();
    }

    @GetMapping("/by_status/{status}")
    public List<Payment> getByStatus(@PathVariable PaymentStatus paymentStatus){
//        return paymentRepository.findAll(PaymentSpecifications.hasStatus(paymentStatus));
        return paymentRepository.findByStatus(paymentStatus);
    }


}