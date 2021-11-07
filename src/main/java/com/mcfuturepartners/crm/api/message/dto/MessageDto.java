package com.mcfuturepartners.crm.api.message.dto;

import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.user.entity.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
public class MessageDto {
    @ApiModelProperty(position = 0, example = "메시지 고유 번호. 저장, 수정, 삭제 시 없어도 됨") private Long messageId;
    @ApiModelProperty(position = 1, example = "메시지 제목") private String title;
    @ApiModelProperty(position = 2, example = "메시지 내용, 수정 시 빈공간으로 보냈을 때 기존 내용 유지") private String content;
    @ApiModelProperty(position = 3, example = "사용자(요청자) 아이디. token 추출 후 서버에서 넣어주기 떄문에 필요 없음")private String username;

    public Message toEntity(){
        return Message.builder()
                .content(this.content)
                .build();
    }
}
