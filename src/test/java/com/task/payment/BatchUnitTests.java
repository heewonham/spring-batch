package com.task.payment;

import org.apache.tomcat.jni.Local;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Transactional
@SpringBootTest
public class BatchUnitTests {

}
