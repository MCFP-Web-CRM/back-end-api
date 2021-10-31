package com.mcfuturepartners.crm.api.schedule.dto;

import com.mcfuturepartners.crm.api.schedule.entity.Schedule;

import java.time.LocalDateTime;

public class ScheduleRegister {

    private String userName;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String contents;
    private Boolean isPublic;

}
