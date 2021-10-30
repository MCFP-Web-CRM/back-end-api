package com.mcfuturepartners.crm.api.order.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderCancelDto {
    @ApiModelProperty(position = 0,example = "사용자 권한 프런트에서 넣을 필요 없음") private String authorities;
    @ApiModelProperty(position = 1,example = "사용자 권한 프런트에서 넣을 필요 없음") private String username;
    @ApiModelProperty(position = 2,example = "구독 주문 id") private Long orderId;
}
