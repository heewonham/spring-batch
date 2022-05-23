package com.task.payment.domain.payload;

import com.task.payment.domain.entity.DataMismatchType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter @Setter
public class RequestParam {
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate verifiedDate;
    private DataMismatchType type;
    @DateTimeFormat(pattern = "yyyyMMdd")
    private LocalDate date;

    public RequestParam(LocalDate verifiedDate, DataMismatchType type, LocalDate date) {
        this.verifiedDate = verifiedDate;
        this.type = type;
        this.date = date;
    }
}
