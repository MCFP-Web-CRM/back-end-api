package com.mcfuturepartners.crm.api.schedule.service;

import com.mcfuturepartners.crm.api.schedule.dto.*;
import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

public interface ScheduleService {
    List<UserScheduleResponse> getAllSchedulesInBetween(ScheduleGet scheduleGet);
    List<UserScheduleResponse> getScheduleInBetween(ScheduleGet scheduleGet);
    ScheduleResponse getSchedule(Long scheduleId, String username, String authority);
    ScheduleResponse saveSchedule(ScheduleRegister scheduleRegister);
    ScheduleResponse updateSchedule(Long scheduleId, ScheduleUpdate scheduleUpdate);
    void deleteSchedule(Long scheduleId, String username);
}
