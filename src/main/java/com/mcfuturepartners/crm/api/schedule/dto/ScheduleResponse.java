package com.mcfuturepartners.crm.api.schedule.dto;

import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
 @NoArgsConstructor
public class ScheduleResponse {
    private Long scheduleId;
    private String title;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String contents;
    private Boolean isPublic;
}
