package com.mcfuturepartners.crm.api.order.dto;

import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor
public class OrderResponseDto {
    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private long customerId;
    @ApiModelProperty(position = 2) private ProductDto product;
}
