package com.mcfuturepartners.crm.api.order.dto;

import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.refund.dto.RefundDto;
import com.mcfuturepartners.crm.api.refund.dto.RefundResponseDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.jni.Local;

import java.time.LocalDateTime;

@Data @NoArgsConstructor
public class OrderResponseDto {
    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private long customerId;
    @ApiModelProperty(position = 2) private ProductDto product;
    @ApiModelProperty(position = 3) private long price;
    @ApiModelProperty(position = 4) private long investmentAmount;
    @ApiModelProperty(position = 5) private RefundResponseDto refundDto;
    @ApiModelProperty(position = 6) private LocalDateTime regDate;
}
