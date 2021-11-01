package com.mcfuturepartners.crm.api.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRevenueDto {
    private UserResponseDto user;
    private Long amount;

}
