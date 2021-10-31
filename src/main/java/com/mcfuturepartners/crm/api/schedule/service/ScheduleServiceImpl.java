package com.mcfuturepartners.crm.api.schedule.service;

import com.mcfuturepartners.crm.api.schedule.dto.ScheduleRegister;
import com.mcfuturepartners.crm.api.schedule.dto.ScheduleUpdate;
import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl implements ScheduleService{

    @Override
    public List<Schedule> getAllSchedule() {
        return null;
    }

    @Override
    public Schedule getSchedule(Long scheduleId) {
        return null;
    }

    @Override
    public List<Schedule> saveSchedule(ScheduleRegister scheduleRegister) {
        return null;
    }

    @Override
    public List<Schedule> updateSchedule(Long scheduleId, ScheduleUpdate scheduleUpdate) {
        return null;
    }
}
