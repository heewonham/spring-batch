package com.task.payment.service;

import com.task.payment.domain.entity.IntegrityResultData;
import com.task.payment.domain.payload.RequestParam;
import com.task.payment.repository.IntegrityResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DataIntegrityService {

    private final IntegrityResultRepository integrityResultRepository;

    public PageImpl<IntegrityResultData> findIntegrityResultData(RequestParam requestParam, Pageable page){
        return integrityResultRepository.searchList(requestParam, page);
    }
}
