package com.mcfuturepartners.crm.api.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRevenueRefundDto {
    private Long salesAmount;
    private Long refundAmount;
    private Long profitAmount;
}
