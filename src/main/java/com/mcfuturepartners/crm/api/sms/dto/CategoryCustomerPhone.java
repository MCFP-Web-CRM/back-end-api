package com.mcfuturepartners.crm.api.sms.dto;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CategoryCustomerPhone {
    private CategoryDto categoryDto;
    private Long customerCount;
}
