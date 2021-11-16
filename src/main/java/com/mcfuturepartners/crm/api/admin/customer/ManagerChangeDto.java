package com.mcfuturepartners.crm.api.admin.customer;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ManagerChangeDto {

    @ApiModelProperty(position = 1, example = "이전 담당자 id") public Long oldManagerId;
    @ApiModelProperty(position = 2, example = "새로운 담당자 id")public Long newManagerId;
}
