package com.mcfuturepartners.crm.api.product.dto;

import com.mcfuturepartners.crm.api.product.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

//도메인 레벨, 혹은 서비스레벨로 내려가야함
//order에서 처리할 예정 수정 필요
@Data
@Builder
public class ProductRevenueDto{
    @ApiModelProperty(position = 1, example = "상품 설명") private ProductDto product;
    @ApiModelProperty(position = 1, example = "상품 매출액")private Long amount;

}
