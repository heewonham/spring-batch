package com.task.payment.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "PAYMENT_SYSTEM")
public class PaymentData {
    @Id
    private long seq;

    @Column(name = "pay_key")
    private String payKey;

    @Column(name = "order_id")
    private String orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_status")
    private PayStatus payStatus;

    private Integer amount;

    @Column(name = "transaction_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionAt;

    @Column(name = "pay_method")
    private String payMethod;
}
