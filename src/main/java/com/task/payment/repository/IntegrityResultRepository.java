package com.task.payment.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.task.payment.domain.entity.DataMismatchType;
import com.task.payment.domain.entity.IntegrityResultData;
import com.task.payment.domain.payload.RequestParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

import static com.task.payment.domain.entity.QIntegrityResultData.*;


@Repository
@Transactional
@RequiredArgsConstructor
public class IntegrityResultRepository {

    private final EntityManager entityManager;

    public PageImpl<IntegrityResultData> searchList(RequestParam param, Pageable page){
        System.out.println(param.getVerifiedDate() + " " + param.getType() + " " + param.getDate());
        List<IntegrityResultData> results = new JPAQueryFactory(entityManager)
                .select(integrityResultData)
                .from(integrityResultData)
                .where(
                        findVerifiedDate(param.getVerifiedDate()),
                        findType(param.getType()),
                        findApprovedOrCanceldDate(param.getDate()))
                .offset(page.getOffset())
                .limit(page.getPageSize())
                .fetch();

        return new PageImpl<>(results, page, results.size());
    }

    private BooleanExpression findVerifiedDate(LocalDate date){
        return date != null ? integrityResultData.verifiedAt.eq(date) : null;
    }

    private BooleanExpression findType(DataMismatchType type){
        return type != null ? integrityResultData.mismatchType.eq(type) : null;
    }

    private BooleanExpression findApprovedOrCanceldDate(LocalDate date){
        return date != null ? integrityResultData.approvedAt.eq(date).or(integrityResultData.canceledAt.eq(date)) : null;
    }

}
