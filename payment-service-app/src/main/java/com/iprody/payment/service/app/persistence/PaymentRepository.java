package com.iprody.payment.service.app.persistence;


import com.iprody.payment.service.app.persistence.entity.Payment;
import com.iprody.payment.service.app.persistence.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
public interface PaymentRepository extends JpaRepository<Payment, UUID>, JpaSpecificationExecutor<Payment> {

    List<Payment> findByStatus(PaymentStatus status);

    @Modifying
    @Transactional
    @Query("update Payment y set y.status = :status where y.guid = :id") // Установка новых значений
    void updateStatus(@Param("id") UUID id, @Param("status") PaymentStatus status); // ID обновляемой записи

    @Modifying
    @Transactional
    @Query("update Payment y set y.note = :note where y.guid = :id") // Установка новых значений
    void updateNote(@Param("id") UUID id, @Param("note") String note); // ID обновляемой записи

}