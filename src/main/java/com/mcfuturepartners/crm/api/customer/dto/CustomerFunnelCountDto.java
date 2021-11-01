package com.mcfuturepartners.crm.api.customer.dto;

import com.mcfuturepartners.crm.api.funnel.dto.FunnelResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerFunnelCountDto {
    @ApiModelProperty(position = 1, example = "유입 경로") private FunnelResponseDto funnel;
    @ApiModelProperty(position = 2, example = "당일 해당 경로를 통해 유입된 고객의 수") private Integer count;
}
