package com.mcfuturepartners.crm.api.revenue.dto;

import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserMonthlyRevenue {
    private Integer year;
    private Integer month;
    private UserResponseDto user;
    private Long amount;
}
