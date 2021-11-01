package com.mcfuturepartners.crm.api.schedule.dto;

import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ScheduleRegister {

    @ApiModelProperty(position = 1, example = "username 필요없음")private String userName;
    @ApiModelProperty(position = 2, example = "일정 제목 필수")private String title;
    @ApiModelProperty(position = 3, example = "일정 시작 날짜시간 필수")private LocalDateTime startDate;
    @ApiModelProperty(position = 4, example = "일정 마감 날짜시간 필수")private LocalDateTime endDate;
    @ApiModelProperty(position = 5, example = "일정 내용")private String contents;
    @ApiModelProperty(position = 6, example = "공개여부 필수") private Boolean isPublic;

    public Schedule toEntity(){
        return Schedule.builder()
                .title(title)
                .contents(contents)
                .startDate(startDate)
                .endDate(endDate)
                .isPublic(isPublic)
                .build();
    }
}
