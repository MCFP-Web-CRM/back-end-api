package com.mcfuturepartners.crm.api.user.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserLoginResponseDto {
    private Long id;
    private String token;
    private String Authority;
    private Long expiredTime;
}
