package com.mcfuturepartners.crm.api.order.repository;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.order.entity.QOrder;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends QuerydslRepositorySupport implements CustomeOrderRepository {
    private final JPAQueryFactory queryFactory;

    public OrderRepositoryImpl(JPAQueryFactory queryFactory){
        super(OrderRepository.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Order> searchOrdersAfterAndBefore(LocalDateTime startDate, LocalDateTime endDate) {
        QOrder order = QOrder.order;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(order.regDate.between(startDate,endDate));



        return null;
    }
}
