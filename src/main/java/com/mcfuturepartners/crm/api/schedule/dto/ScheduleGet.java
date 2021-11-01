package com.mcfuturepartners.crm.api.schedule.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleGet {
    @ApiModelProperty(position = 1, example = "사원")private String username;
    @ApiModelProperty(position = 2, example = "캘린더 시작 날짜")private LocalDateTime calendarStartDate;
    @ApiModelProperty(position = 6, example = "캘린더 끝 날짜")private LocalDateTime calendarEndDate;
}
