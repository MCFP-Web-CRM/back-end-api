package com.mcfuturepartners.crm.api.product.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRevenue {
    private String productName;
    private long dailySales;
    private long monthlySales;
}
