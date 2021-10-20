package com.mcfuturepartners.crm.api.counsel.dto;

import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Date;

@Data
@Component
@NoArgsConstructor
public class CounselDto {
    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private String customerId;
    @ApiModelProperty(position = 2) private String productId;
    @ApiModelProperty(position = 3) private String status;
    @ApiModelProperty(position = 4) private String contents;

    private User user;

    public Counsel toEntity(){
       return Counsel.builder()
                .customer(customerId)
                .product(productId)
                .user(user)
                .status(status)
                .contents(contents)
                .regDate(new Date())
                .build();
    }


}
