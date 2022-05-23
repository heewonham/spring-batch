package com.task.payment.repository;

import com.task.payment.domain.entity.PaymentData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentData, String> {

    List<PaymentData> findByPayKey(String payKey);
}