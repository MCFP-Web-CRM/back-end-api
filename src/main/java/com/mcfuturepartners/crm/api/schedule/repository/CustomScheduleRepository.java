package com.mcfuturepartners.crm.api.schedule.repository;

import com.mcfuturepartners.crm.api.schedule.entity.Schedule;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomScheduleRepository {
    List<Schedule> findAllByStartDateOrEndDateIsBetween(LocalDateTime calendarStartDate, LocalDateTime calendarEndDate);
}
