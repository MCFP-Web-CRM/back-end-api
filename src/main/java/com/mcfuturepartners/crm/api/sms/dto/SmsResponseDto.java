package com.mcfuturepartners.crm.api.sms.dto;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.message.dto.MessageDto;
import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.sms.entity.SmsStatus;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data @Builder
@NoArgsConstructor @AllArgsConstructor
public class SmsResponseDto {
    private Long smsId;

    private MessageDto message;

    private SmsStatus smsStatus;

    private String senderName;

    private LocalDateTime sendTime;

}
