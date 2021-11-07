package com.mcfuturepartners.crm.api.sms.repository;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomCustomerRepository;
import com.mcfuturepartners.crm.api.sms.entity.QSms;
import com.mcfuturepartners.crm.api.sms.entity.Sms;
import com.mcfuturepartners.crm.api.sms.entity.SmsStatus;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.xpath.operations.Bool;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;

public class SmsRepositoryImpl extends QuerydslRepositorySupport implements CustomSmsRepository {
    private final JPAQueryFactory queryFactory;

    public SmsRepositoryImpl(JPAQueryFactory queryFactory){
        super(Sms.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Sms> findReservedSmsBeforeNow(LocalDateTime processTime) {
        QSms sms = QSms.sms;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(sms.smsStatus.eq(SmsStatus.RESERVED).and(sms.sendTime.before(processTime)));

        return queryFactory.selectFrom(sms)
                .where(booleanBuilder)
                .fetch();
    }
}
