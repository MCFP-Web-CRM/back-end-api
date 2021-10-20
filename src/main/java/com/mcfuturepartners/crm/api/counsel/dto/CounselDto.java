package com.mcfuturepartners.crm.api.counsel.dto;

import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class CounselDto {
    private final UserRepository userRepository;
    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private String customerId;
    //@ApiModelProperty(position = 2) private String userId;
    @ApiModelProperty(position = 3) private String productId;
    @ApiModelProperty(position = 4) private String status;
    @ApiModelProperty(position = 5) private String contents;
    @ApiModelProperty(position = 6) private String regDate;


    public Counsel toEntity(){
       return Counsel.builder()
                .customerId(customerId)
                .productId(productId)
                .status(status)
                .contents(contents)
                .regDate(new Date())
                .build();
    }
}
