package com.mcfuturepartners.crm.api.admin.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckManagerChangeDto {

    @ApiModelProperty(position = 1, example = "이전 담당자 id") public List<Long> customerIds;
    @ApiModelProperty(position = 2, example = "새로운 담당자 id")public Long newManagerId;
}
