package com.mcfuturepartners.crm.api.revenue.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductMonthlyRequest {
    Integer month;
    List<Long> productIds;
}
