package com.mcfuturepartners.crm.api.sms.dto;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductCustomerPhone {
    private ProductDto productDto;
    private Long customerCount;
}
