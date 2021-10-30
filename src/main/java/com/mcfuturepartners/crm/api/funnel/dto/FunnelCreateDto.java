package com.mcfuturepartners.crm.api.funnel.dto;

import com.mcfuturepartners.crm.api.funnel.entity.Funnel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@NoArgsConstructor
public class FunnelCreateDto {
    @ApiModelProperty(position = 1, example = "유입경로 항목")private String funnelName;

    public Funnel toEntity(){
        return Funnel.builder()
                .funnelName(funnelName)
                .build();
    }
}
