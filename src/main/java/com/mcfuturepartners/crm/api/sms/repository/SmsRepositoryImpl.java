package com.mcfuturepartners.crm.api.sms.repository;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomCustomerRepository;
import com.mcfuturepartners.crm.api.sms.entity.Sms;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class SmsRepositoryImpl extends QuerydslRepositorySupport implements CustomSmsRepository {
    private final JPAQueryFactory queryFactory;

    public SmsRepositoryImpl(JPAQueryFactory queryFactory){
        super(Sms.class);
        this.queryFactory = queryFactory;
    }

}
