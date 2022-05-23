package com.task.payment.batch.writer;

import com.task.payment.domain.entity.CardData;
import com.task.payment.domain.entity.IntegrityResultData;
import com.task.payment.domain.entity.PaymentData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Configuration
@RequiredArgsConstructor
public class JpaItemWriters {

    private final EntityManagerFactory entityManagerFactory;

    @Bean
    public JpaItemWriter<CardData> jpaCardItemWriter() {
        return new JpaItemWriterBuilder<CardData>()
                .usePersist(true)
                .entityManagerFactory(this.entityManagerFactory)
                .build();
    }

    @Bean
    public JpaItemWriter<PaymentData> jpaPaymentItemWriter() {
        return new JpaItemWriterBuilder<PaymentData>()
                .usePersist(true)
                .entityManagerFactory(this.entityManagerFactory)
                .build();
    }


    public JpaItemListWriter<IntegrityResultData> writerList() {
        JpaItemWriter<IntegrityResultData> writer = new JpaItemWriter<>();
        writer.setEntityManagerFactory(entityManagerFactory);
        return new JpaItemListWriter<>(writer);
    }
}
