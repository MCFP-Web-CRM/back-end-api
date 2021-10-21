package com.mcfuturepartners.crm.api.order.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class OrderCancelDto {
    private String authorities;
    private String username;
    private long orderId;
}
