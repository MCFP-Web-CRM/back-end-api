package com.mcfuturepartners.crm.api.revenue.dto;

import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductMonthlyRevenue {
    private Integer year;
    private Integer month;
    private ProductDto product;
    private Long amount;
}
