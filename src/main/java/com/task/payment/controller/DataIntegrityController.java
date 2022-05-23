package com.task.payment.controller;

import com.task.payment.domain.entity.IntegrityResultData;
import com.task.payment.domain.payload.RequestParam;
import com.task.payment.service.DataIntegrityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/integrity")
@RequiredArgsConstructor
public class DataIntegrityController {

    private final DataIntegrityService service;

    @GetMapping("/search")
    public ResponseEntity<PageImpl<IntegrityResultData>> getData(RequestParam requestParam, Pageable page){
        return ResponseEntity.ok().body(service.findIntegrityResultData(requestParam, page));
    }
}
