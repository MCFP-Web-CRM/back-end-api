package com.mcfuturepartners.crm.api.product.dto;

import com.mcfuturepartners.crm.api.product.dto.ProductRevenueDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ProductRevenueResponseDto {
    @ApiModelProperty(position = 1, example = "월 혹은 일 단위") private String unit;
    @ApiModelProperty(position = 1, example = "월, 일에 따른 상품별 매출액") private List<ProductRevenueDto> productRevenueList;
}
