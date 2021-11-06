package com.mcfuturepartners.crm.api.product.dto;

import com.mcfuturepartners.crm.api.product.entity.Product;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
@Data
@NoArgsConstructor
public class ProductDto {
    @ApiModelProperty(position = 0, example = "상품 고유 id / 지금 필요 없음") private Long id;
    @ApiModelProperty(position = 1, example = "상품명") private String name;
    @ApiModelProperty(position = 2, example = "상품가격") private Long price;

    public Product toEntity(){
        return Product.builder()
                .id(id)
                .name(name)
                .price(price)
                .build();
    }

}
