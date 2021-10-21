package com.mcfuturepartners.crm.api.user.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRevenue {
    private String name;
    private long dailySales;
    private long monthlySales;
}
