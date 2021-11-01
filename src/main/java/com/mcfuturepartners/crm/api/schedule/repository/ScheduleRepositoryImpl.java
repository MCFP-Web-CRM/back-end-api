package com.mcfuturepartners.crm.api.schedule.repository;

import com.mcfuturepartners.crm.api.schedule.entity.QSchedule;
import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import com.mcfuturepartners.crm.api.schedule.service.ScheduleServiceImpl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class ScheduleRepositoryImpl extends QuerydslRepositorySupport implements CustomScheduleRepository{
    private final JPAQueryFactory queryFactory;

    public ScheduleRepositoryImpl(JPAQueryFactory queryFactory){
        super(Schedule.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public List<Schedule> findAllByStartDateOrEndDateIsBetween(LocalDateTime calendarStartDate, LocalDateTime calendarEndDate) {
        QSchedule schedule = QSchedule.schedule;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(schedule.startDate.between(calendarStartDate,calendarEndDate)).or(schedule.endDate.between(calendarStartDate,calendarEndDate));

        JPQLQuery<Schedule> query = queryFactory.selectFrom(schedule).where(booleanBuilder);

        return query.fetch();
    }
}
