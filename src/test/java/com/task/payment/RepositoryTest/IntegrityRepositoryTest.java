package com.task.payment.RepositoryTest;

import com.task.payment.domain.entity.DataMismatchType;
import com.task.payment.domain.entity.IntegrityResultData;
import com.task.payment.domain.payload.RequestParam;
import com.task.payment.repository.IntegrityResultRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class IntegrityRepositoryTest {

    @Autowired
    private IntegrityResultRepository repository;

    @Test
    public void searchTest(){
        RequestParam param = new RequestParam(null, DataMismatchType.MISSING,null);
        Pageable page = Pageable.ofSize(10).withPage(0);
        PageImpl<IntegrityResultData> list = repository.searchList(param, page);
        Assertions.assertThat(list.getContent().size()).isEqualTo(3);
    }
}
