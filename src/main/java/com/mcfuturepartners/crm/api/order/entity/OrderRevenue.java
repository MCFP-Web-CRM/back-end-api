package com.mcfuturepartners.crm.api.order.entity;

import lombok.Builder;
import lombok.Data;

@Builder @Data
public class OrderRevenue {
    private long currentRevenue;
}
