package com.mcfuturepartners.crm.api.util.sms;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
@Data
public class Sms {
    @ApiModelProperty(position = 1, example = "수신자 전화번호 리스트 (배열 형태로 전달)") private List<String> receiverPhone;
    @ApiModelProperty(position = 2, example = "문자 내용 전송") private String content;
}
