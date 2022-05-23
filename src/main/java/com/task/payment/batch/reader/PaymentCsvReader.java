package com.task.payment.batch.reader;

import com.task.payment.domain.entity.PaymentData;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.core.io.ClassPathResource;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.TimeZone;

@Configuration
@RequiredArgsConstructor
public class PaymentCsvReader {
    @Bean
    public ConversionService customPaymentSystemConversionService() {
        DefaultConversionService customConversionService = new DefaultConversionService();
        DefaultConversionService.addDefaultConverters(customConversionService);
        customConversionService.addConverter(new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String text){
                long time = Long.parseLong(text);
                return LocalDateTime.ofInstant(Instant.ofEpochSecond(time),
                        TimeZone.getDefault().toZoneId());
            }
        });
        return customConversionService;
    }


    @Bean
    public FlatFileItemReader<PaymentData> paymentCsvFileItemReader()  {
        /* file read */
        FlatFileItemReader<PaymentData> flatFileItemReader = new FlatFileItemReader<>();

        flatFileItemReader.setResource(new ClassPathResource("/csv/payment.csv"));
        flatFileItemReader.setLinesToSkip(1); // 1번째 line skip
        flatFileItemReader.setEncoding("UTF-8"); // encoding

        /* read하는 데이터를 내부적으로 LineMapper을 통해 Mapping */
        DefaultLineMapper<PaymentData> defaultLineMapper = new DefaultLineMapper<>();

        /* delimitedLineTokenizer : setNames를 통해 각각의 데이터의 이름 설정 */
        DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer(",");
        delimitedLineTokenizer.setNames("seq","pay_key","order_id","pay_status","amount","transaction_at","pay_method");
        defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);

        /* beanWrapperFieldSetMapper : Tokenizer에서 가지고온 데이터들을 VO로 바인드하는 역할 */
        BeanWrapperFieldSetMapper<PaymentData> beanWrapperFieldSetMapper = new BeanWrapperFieldSetMapper<>();
        beanWrapperFieldSetMapper.setConversionService(customPaymentSystemConversionService());
        beanWrapperFieldSetMapper.setTargetType(PaymentData.class);
        defaultLineMapper.setFieldSetMapper(beanWrapperFieldSetMapper);

        /* lineMapper 지정 */
        flatFileItemReader.setLineMapper(defaultLineMapper);

        return flatFileItemReader;
    }
}
