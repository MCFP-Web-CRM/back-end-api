package com.mcfuturepartners.crm.api.schedule.dto;

import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserScheduleResponse {
    @ApiModelProperty(position = 1, example = "사원")private UserResponseDto user;
    @ApiModelProperty(position = 2, example = "지정된 기간동안 사원의 스케줄") private List<ScheduleResponse> scheduleList;
}
