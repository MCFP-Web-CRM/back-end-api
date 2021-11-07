package com.mcfuturepartners.crm.api.customer.repository;

import com.mcfuturepartners.crm.api.category.entity.QCategory;
import com.mcfuturepartners.crm.api.counsel.entity.QCounsel;
import com.mcfuturepartners.crm.api.customer.dto.CustomerSearch;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.entity.QCustomer;
import com.mcfuturepartners.crm.api.funnel.entity.Funnel;
import com.mcfuturepartners.crm.api.order.entity.QOrder;
import com.mcfuturepartners.crm.api.user.entity.QUser;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryFactory;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class CustomerRepositoryImpl extends QuerydslRepositorySupport implements CustomCustomerRepository {
    private final JPAQueryFactory queryFactory;
    
    public CustomerRepositoryImpl(JPAQueryFactory queryFactory){
        super(Customer.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Customer> search(CustomerSearch customerSearch, Pageable pageable){

        QCustomer customer = QCustomer.customer;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(StringUtils.hasText(customerSearch.getCategoryName())){
            booleanBuilder.and(customer.category.name.eq(customerSearch.getCategoryName()));
        }

        if(!ObjectUtils.isEmpty(customerSearch.getFunnelId())){
            booleanBuilder.and(customer.funnel.funnelId.eq(customerSearch.getFunnelId()));
        }
        if(StringUtils.hasText(customerSearch.getProductName())){
            booleanBuilder.and(customer.orders.any().product.name.eq(customerSearch.getProductName()));
        }
        if(StringUtils.hasText(customerSearch.getCounselKeyword())){
            booleanBuilder.and(customer.counsels.any().contents.contains(customerSearch.getCounselKeyword()));
        }
        if(!ObjectUtils.isEmpty(customerSearch.getStartDate())){
            booleanBuilder.and(customer.counsels.any().regDate.after(customerSearch.getStartDate().atStartOfDay()));
        }
        if(!ObjectUtils.isEmpty(customerSearch.getEndDate())){
            booleanBuilder.and(customer.counsels.any().regDate.before(customerSearch.getEndDate().atTime(23,59,59)));
        }
        if(!ObjectUtils.isEmpty(customerSearch.getManagerId())){
            booleanBuilder.and(customer.manager.id.eq(customerSearch.getManagerId()));
        }
        JPQLQuery<Customer> query =  queryFactory.selectFrom(customer).where(booleanBuilder);
        long totalCount = query.fetchCount();
        List<Customer> results = getQuerydsl().applyPagination(pageable, query).fetch();
        return new PageImpl<>(results,pageable,totalCount);
    }

    @Override
    public List<Customer> searchWithoutPageable(CustomerSearch customerSearch) {
        QCustomer customer = QCustomer.customer;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if(StringUtils.hasText(customerSearch.getCategoryName())){
            booleanBuilder.and(customer.category.name.eq(customerSearch.getCategoryName()));
        }

        if(!ObjectUtils.isEmpty(customerSearch.getFunnelId())){
            booleanBuilder.and(customer.funnel.funnelId.eq(customerSearch.getFunnelId()));
        }
        if(StringUtils.hasText(customerSearch.getProductName())){
            booleanBuilder.and(customer.orders.any().product.name.eq(customerSearch.getProductName()));
        }
        if(StringUtils.hasText(customerSearch.getCounselKeyword())){
            booleanBuilder.and(customer.counsels.any().contents.contains(customerSearch.getCounselKeyword()));
        }
        if(!ObjectUtils.isEmpty(customerSearch.getStartDate())){
            booleanBuilder.and(customer.counsels.any().regDate.after(customerSearch.getStartDate().atStartOfDay()));
        }
        if(!ObjectUtils.isEmpty(customerSearch.getEndDate())){
            booleanBuilder.and(customer.counsels.any().regDate.before(customerSearch.getEndDate().atTime(23,59,59)));
        }
        if(!ObjectUtils.isEmpty(customerSearch.getManagerId())){
            booleanBuilder.and(customer.manager.id.eq(customerSearch.getManagerId()));
        }
        JPQLQuery<Customer> query =  queryFactory.selectFrom(customer).where(booleanBuilder);


        return query.fetch();
    }

    @Override
    public List<Customer> findCustomersWithCounselToday(LocalDateTime localDateTime) {
        QCustomer customer = QCustomer.customer;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(customer.counsels.any().regDate.after(localDateTime));

        return queryFactory.selectFrom(customer)
                .where(booleanBuilder)
                .fetch();
    }
    @Override
    public List<Customer> findCustomersWithOrderToday(LocalDateTime localDateTime){
        QCustomer customer = QCustomer.customer;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(customer.orders.any().regDate.after(localDateTime));

        return queryFactory.selectFrom(customer)
                .where(booleanBuilder)
                .fetch();
    }

    @Override
    public Integer countCustomersByFunnel(LocalDateTime localDateTime, Funnel funnel) {
        QCustomer customer = QCustomer.customer;

        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(customer.regDate.after(localDateTime));
        booleanBuilder.and(customer.funnel.funnelId.eq(funnel.getFunnelId()));

        return (int) queryFactory.selectFrom(customer)
                .where(booleanBuilder)
                .fetchCount();
    }


}
