package com.task.payment.domain.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name="INTEGRITY_RESULT")
public class IntegrityResultData {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "pay_key")
    private String payKey;

    @Enumerated(EnumType.STRING)
    @Column(name = "mismatch_type")
    private DataMismatchType mismatchType;

    @Column(name = "verified_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate verifiedAt;

    @Column(name = "approved_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate approvedAt;

    @Column(name = "canceled_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate canceledAt;

    public IntegrityResultData(String payKey, DataMismatchType mismatchType, LocalDate verifiedAt, LocalDate approvedAt, LocalDate canceledAt) {
        this.payKey = payKey;
        this.mismatchType = mismatchType;
        this.verifiedAt = verifiedAt;
        this.approvedAt = approvedAt;
        this.canceledAt = canceledAt;
    }

    public IntegrityResultData() {

    }
}
