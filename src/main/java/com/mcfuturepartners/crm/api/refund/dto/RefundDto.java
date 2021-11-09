package com.mcfuturepartners.crm.api.refund.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@Builder
public class RefundDto {
    @ApiModelProperty(position = 1, example = "주문 id") private long orderId;
    @ApiModelProperty(position = 2, example = "환불 금액") private long amount;
}
