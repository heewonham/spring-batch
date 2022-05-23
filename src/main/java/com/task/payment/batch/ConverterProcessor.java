package com.task.payment.batch;

import com.task.payment.domain.entity.*;
import com.task.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ConverterProcessor {

    private final PaymentRepository paymentRepository;
    private final RequestDateJobParameter jobParameter;

    @Bean
    public ItemProcessor<CardData, List<IntegrityResultData>> processor(){

        return o -> {
            return findIntegrity(o);
        };
    }
    private List<IntegrityResultData> findIntegrity(CardData cardData){

        List<PaymentData> paymentDataList = paymentRepository.findByPayKey(cardData.getTid());
        List<IntegrityResultData> result = new ArrayList<>();
        paymentDataList.sort((o1, o2) -> { return o1.getPayStatus().ordinal() - o2.getPayStatus().ordinal(); });

        // 1. 서로의 데이터 중 누락된 것이 있는가? (데이터 유실, 과정이 누락되는가?)
        if(checkOmission(paymentDataList)){
            result.add(makeIntegrityResultData(cardData, DataMismatchType.MISSING));
        }

        // 2. approve / cancel 요청 금액이 일치하는가?
        if(!checkAmount(paymentDataList, cardData)) {
            result.add(makeIntegrityResultData(cardData, DataMismatchType.DIFF_AMOUNT));
        }

        // 3. 결제 상태가 일치하는가?
        if(!checkStaus(paymentDataList,cardData.getStatus())){
            result.add(makeIntegrityResultData(cardData, DataMismatchType.DIFF_STATUS));
        }

        // 4. 결제 수단이 일치하는가?
        if(!checkMethod(paymentDataList, cardData)){
            result.add(makeIntegrityResultData(cardData, DataMismatchType.DIFF_METHOD));
        }

        // 5. Approve / cancel 일시가 일치하는가?
        if(!checkDateTime(paymentDataList, cardData)){
            result.add(makeIntegrityResultData(cardData, DataMismatchType.DIFF_DATETIME));
        }

        if(result.size() == 0) return null;
        return result;
    }

    private boolean checkOmission(List<PaymentData> paymentDataList){
        int size = paymentDataList.size();
        if(size <= 1 || paymentDataList.size() > 4) return true;
        if(!paymentDataList.get(0).getPayStatus().equals(PayStatus.READY)) return true;
        if(!paymentDataList.get(1).getPayStatus().equals(PayStatus.APPROVE)) return true;
        if(size == 3 && !paymentDataList.get(2).getPayStatus().equals(PayStatus.CANCEL)) return true; // 마지막이 Error인 경우 카드 결제 내역에 없어야함. 있다는 것은 cancel이 누락되었음을 암시
        if(size == 4){
            if(!paymentDataList.get(2).getPayStatus().equals(PayStatus.CANCEL)) return true;
            if(!paymentDataList.get(3).getPayStatus().equals(PayStatus.ERROR)) return true;
        }
        return false;
    }

    private boolean checkAmount(List<PaymentData> paymentDataList, CardData cardData){
        for (PaymentData paymentData : paymentDataList) {
            if(paymentData.getPayStatus().equals(PayStatus.APPROVE) || paymentData.getPayStatus().equals(PayStatus.CANCEL)){
                if(!paymentData.getAmount().equals(cardData.getPaymentAmount())){
                    return false;
                }
            }
        }
        return true;
    }

    private boolean checkStaus(List<PaymentData> paymentDataList, int status){
        int last = paymentDataList.size()-1;
        PayStatus payStatus = paymentDataList.get(last).getPayStatus();
        // 마지막이 approve 이면 0
        if(payStatus.equals(PayStatus.APPROVE) && status == 0) return true;
        // 마지막이 cancel 이면 1
        if(payStatus.equals(PayStatus.CANCEL) && status == 1) return true;
        // 마지막이 error 이면 2번째 전 확인
        if(payStatus.equals(PayStatus.ERROR)){
            if(last-2 >= 0 && paymentDataList.get(last-2).getPayStatus().equals(PayStatus.APPROVE) && status == 0) return true;
        }
        return false;
    }

    private boolean checkMethod(List<PaymentData> paymentDataList, CardData cardData){
        for (PaymentData paymentData : paymentDataList) {
            if(!paymentData.getPayMethod().contains(cardData.getPaymentMethod())){
                return false;
            }
        }
        return true;
    }

    private boolean checkDateTime(List<PaymentData> paymentDataList, CardData cardData){
        for (PaymentData paymentData : paymentDataList) {
            if(paymentData.getPayStatus().equals(PayStatus.APPROVE)){
                if(!paymentData.getTransactionAt().equals(cardData.getApprovedAt())) return false;
            }
            if(paymentDataList.size() < 4 && paymentData.getPayStatus().equals(PayStatus.CANCEL)){
                if(!paymentData.getTransactionAt().equals(cardData.getCanceledAt())) return false;
            }
        }
        return true;
    }

    private IntegrityResultData makeIntegrityResultData(CardData cardData,DataMismatchType mismatchType){
        return new IntegrityResultData(
                cardData.getTid(),
                mismatchType,
                jobParameter.getRequestDate(),
                cardData.getApprovedAt().toLocalDate(),
                (cardData.getCanceledAt() == null)? null : cardData.getCanceledAt().toLocalDate()
        );
    }

}
