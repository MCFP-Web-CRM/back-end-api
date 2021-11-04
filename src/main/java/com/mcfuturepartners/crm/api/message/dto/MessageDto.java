package com.mcfuturepartners.crm.api.message.dto;

import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class MessageDto {
    private Long messageId;
    private String content;
    private String username;

    public Message toEntity(){
        return Message.builder()
                .content(this.content)
                .build();
    }
}
