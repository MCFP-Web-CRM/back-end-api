package com.mcfuturepartners.crm.api.sms.dto;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PhoneListDto {
    private CategoryDto category;
    private ProductDto product;
    private List<String> receiverPhone;
}
