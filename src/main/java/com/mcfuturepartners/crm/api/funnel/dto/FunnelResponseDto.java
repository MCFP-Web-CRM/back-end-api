package com.mcfuturepartners.crm.api.funnel.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.Column;

@Data
@Component
@NoArgsConstructor
public class FunnelResponseDto {
    @ApiModelProperty(position = 1, example = "유입경로 id")private Long funnelId;
    @ApiModelProperty(position = 1, example = "유입경로 항목")private String funnelName;

}
