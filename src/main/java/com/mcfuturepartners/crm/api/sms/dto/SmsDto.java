package com.mcfuturepartners.crm.api.sms.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
@Data
public class SmsDto {
    @ApiModelProperty(position = 1, example = "수신자 전화번호 리스트 (배열 형태로 전달)") private List<String> receiverPhone;
    @ApiModelProperty(position = 2, example = "문자 제목") private String title;
    @ApiModelProperty(position = 2, example = "문자 내용 전송") private String content;
    @ApiModelProperty(position = 3, example = "문자 예약 전송(바로 전송일 시 넣지 않고 전달)") private LocalDateTime reservationTime;
}
