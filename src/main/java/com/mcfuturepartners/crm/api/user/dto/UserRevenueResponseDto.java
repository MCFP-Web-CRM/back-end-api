package com.mcfuturepartners.crm.api.user.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class UserRevenueResponseDto {
    private String unit;
    private List<UserRevenueDto> userRevenueList;
}
