package com.mcfuturepartners.crm.api.counsel.dto;

import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class CounselResponseDto {
    @ApiModelProperty(position = 0, example = "상담 데이터 id(추가 api 수행 시 불필요)") private long id;
    @ApiModelProperty(position = 1, example = "고객 ID") private long customerId;
    @ApiModelProperty(position = 2, example = "상담 상태") private String status;
    @ApiModelProperty(position = 3, example = "상담 내용") private String contents;
    @ApiModelProperty(position = 4, example = "상담사(영업사원) username") private UserResponseDto user;
    @ApiModelProperty(position = 5, example = "상담 등록 기간") private LocalDateTime regDate;

}
