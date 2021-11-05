package com.mcfuturepartners.crm.api.schedule.dto;

import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import com.mcfuturepartners.crm.api.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.sql.Update;
import org.springframework.util.StringUtils;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
@Data
public class ScheduleUpdate {
    @ApiModelProperty(position = 0, example = "스케줄 id 필요 없음") private Long schedulId;

    @ApiModelProperty(position = 1, example = "제목 필수 X") private String title;

    @ApiModelProperty(position = 2, example = "일정 시작 시간 필수 X")private LocalDateTime startDate;

    @ApiModelProperty(position = 3, example = "일정 마감 시간 필수 X")private LocalDateTime endDate;

    @ApiModelProperty(position = 4, example = "일정 내용 필수 X")private String contents;

    @ApiModelProperty(position = 5, example = "일정 공개여부 필수 X")private Boolean isPublic;

    @ApiModelProperty(position = 6, example = "사용자 username. 요청 시 넣지 않아도 됨") private String username;
}
