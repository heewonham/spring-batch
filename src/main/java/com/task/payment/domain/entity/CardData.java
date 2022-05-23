package com.task.payment.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Getter @Setter
@Table(name = "CARD_SYSTEM")
public class CardData {
    @Id
    private String tid;

    @Column(name= "approved_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;

    @Column(name= "canceled_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime canceledAt;

    private Integer status;

    @Column(name= "fee_ratio")
    private Double feeRatio;

    @Column(name= "fee_type")
    private String feeType;

    private Integer vat;

    private Integer supply;

    @Column(name= "payment_amount")
    private Integer paymentAmount;

    @Column(name= "payment_method")
    private String paymentMethod;

}
