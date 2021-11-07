package com.mcfuturepartners.crm.api.sms.dto;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PhoneListDto {
    private CategoryDto category;
    private List<String> receiverPhone;
}
