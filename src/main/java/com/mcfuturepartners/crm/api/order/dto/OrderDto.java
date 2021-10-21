package com.mcfuturepartners.crm.api.order.dto;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@NoArgsConstructor
public class OrderDto {
    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private long customerId;
    @ApiModelProperty(position = 2) private long productId;
    private String username;
}
