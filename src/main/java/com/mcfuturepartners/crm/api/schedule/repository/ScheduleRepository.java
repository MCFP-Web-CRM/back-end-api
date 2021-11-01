package com.mcfuturepartners.crm.api.schedule.repository;

import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
}
