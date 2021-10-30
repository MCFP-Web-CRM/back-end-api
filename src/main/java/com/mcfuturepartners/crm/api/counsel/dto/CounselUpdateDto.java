package com.mcfuturepartners.crm.api.counsel.dto;

import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.counsel.entity.CounselStatus;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;

@Data
@NoArgsConstructor
public class CounselUpdateDto {
    @ApiModelProperty(position = 0, example = "상담 데이터 id(추가 api 수행 시 불필요)") private long id;
    @ApiModelProperty(position = 1, example = "고객 ID") private long customerId;
    @ApiModelProperty(position = 2, example = "상담 상태") private String status;
    @ApiModelProperty(position = 3, example = "상담 내용") private String contents;
    @ApiModelProperty(position = 4, example = "상담사(영업사원) username") private String username;

    public Counsel toEntity(){
        return Counsel.builder()
                .status(Arrays.stream(CounselStatus.values()).filter(counselStatus -> counselStatus.getStatus().equals(status)).findFirst().get())
                .contents(contents)
                .build();
    }
}
