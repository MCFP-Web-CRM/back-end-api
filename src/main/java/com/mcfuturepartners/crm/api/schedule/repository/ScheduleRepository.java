package com.mcfuturepartners.crm.api.schedule.repository;

import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
}
