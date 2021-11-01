package com.mcfuturepartners.crm.api.customer.dto;

import com.mcfuturepartners.crm.api.customer.entity.CustomerStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
public class CustomerStatusCountDto {
    @ApiModelProperty(position = 1, example = "고객추가, 상담, 결제추가를 바탕으로 당일 변화된 고객의 상태를 체크") private CustomerStatus customerStatus;
    @ApiModelProperty(position = 2, example = "변화된 인원 count")private Integer count;
}
