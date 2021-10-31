package com.mcfuturepartners.crm.api.schedule.service;

import com.mcfuturepartners.crm.api.schedule.dto.ScheduleRegister;
import com.mcfuturepartners.crm.api.schedule.dto.ScheduleUpdate;
import com.mcfuturepartners.crm.api.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    List<Schedule> getAllSchedule();
    Schedule getSchedule(Long scheduleId);
    List<Schedule> saveSchedule(ScheduleRegister scheduleRegister);
    List<Schedule> updateSchedule(Long scheduleId, ScheduleUpdate scheduleUpdate);
}
