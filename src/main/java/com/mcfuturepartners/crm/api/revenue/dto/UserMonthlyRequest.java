package com.mcfuturepartners.crm.api.revenue.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class UserMonthlyRequest {
    Integer month;
    List<Long> userIds;
}
