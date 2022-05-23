package com.task.payment.batch;

import com.task.payment.batch.reader.CardCsvReader;
import com.task.payment.batch.reader.PaymentCsvReader;

import com.task.payment.batch.writer.JpaItemListWriter;
import com.task.payment.batch.writer.JpaItemWriters;
import com.task.payment.domain.entity.CardData;
import com.task.payment.domain.entity.IntegrityResultData;
import com.task.payment.domain.entity.PaymentData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.persistence.criteria.CriteriaBuilder;
import javax.sql.DataSource;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class FileReaderJobConfig {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final DataSource dataSource;

    private final PaymentCsvReader paymentCsvReader;
    private final CardCsvReader cardCsvReader;
    private final JpaItemWriters jpaItemWriters;
    private final ConverterProcessor converterProcessor;

    private static final int CHUNK_SIZE = 1000;

    @Bean
    @StepScope
    public RequestDateJobParameter jobParameter(@Value("#{jobParameters[requestDate]}") String requestDate){
        return new RequestDateJobParameter(requestDate);
    }

    @Bean
    public Job paymentSystemJob() {
        return jobBuilderFactory.get("paymentSystemJob")
                .preventRestart()
                .start(cardCsvFileReaderStep())
                .next(paymentCsvFileReaderStep())
                .next(systemIntegrityStep())
                .build();
    }

    @Bean
    public Step cardCsvFileReaderStep(){
        return stepBuilderFactory.get("cardCsvFileReaderStep")
                .<CardData, CardData>chunk(CHUNK_SIZE)
                .reader(cardCsvReader.cardCsvFileItemReader())
                .writer(jpaItemWriters.jpaCardItemWriter())
                .build();
    }

    @Bean
    public Step paymentCsvFileReaderStep(){
        return stepBuilderFactory.get("paymentCsvFileReaderStep")
                .<PaymentData,PaymentData>chunk(CHUNK_SIZE)
                .reader(paymentCsvReader.paymentCsvFileItemReader())
                .writer(jpaItemWriters.jpaPaymentItemWriter())
                .build();
    }

    @Bean
    public Step systemIntegrityStep(){
        return stepBuilderFactory.get("systemIntegrityStep")
                .<CardData, List<IntegrityResultData>>chunk(CHUNK_SIZE)
                .reader(jdbcCursorItemReader())
                .processor(converterProcessor.processor())
                .writer(jpaItemWriters.writerList())
                .build();
    }

    @Bean
    public JdbcCursorItemReader<CardData> jdbcCursorItemReader() {
        return new JdbcCursorItemReaderBuilder<CardData>()
                .fetchSize(CHUNK_SIZE)
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(CardData.class))
                .sql("SELECT * FROM CARD_SYSTEM")
                .name("jdbcCursorItemReader")
                .build();
    }
}
