package com.mcfuturepartners.crm.api.sms.dto;

import com.mcfuturepartners.crm.api.message.entity.Message;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
@Builder
@Data
@NoArgsConstructor @AllArgsConstructor
public class SmsProcessDto {
    private Message message;
    private List<String> receiverPhone;
    private List<Long> smsIds;
    private String username;
    private LocalDateTime reservationTime;
}
