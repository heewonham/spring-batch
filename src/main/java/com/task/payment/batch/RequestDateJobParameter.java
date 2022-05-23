package com.task.payment.batch;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
public class RequestDateJobParameter {
    private LocalDate requestDate;

    public RequestDateJobParameter(String requestDateStr){
        this.requestDate = LocalDate.parse(requestDateStr, DateTimeFormatter.ofPattern("yyyyMMdd"));
    }
}
