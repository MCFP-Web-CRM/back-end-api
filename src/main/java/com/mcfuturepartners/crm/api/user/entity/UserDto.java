package com.mcfuturepartners.crm.api.user.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data @Component
public class UserDto {
    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private String username;
    @ApiModelProperty(position = 2) private String password;
}