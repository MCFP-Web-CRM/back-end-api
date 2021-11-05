package com.mcfuturepartners.crm.api.order.dto;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDto {
    @ApiModelProperty(position = 0, example = "구독 결제 id / 필요 없음") private Long id;
    @ApiModelProperty(position = 1, example = "고객 id") private long customerId;
    @ApiModelProperty(position = 2, example = "상품 id") private long productId;
    @ApiModelProperty(position = 3, example = "사원 username / 필요 없음") private String username;
    @ApiModelProperty(position = 3, example = "사원 권한 / 필요 없음") private String authorities;
}
