package com.mcfuturepartners.crm.api.counsel.dto;

import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class UserCounselDto {
    private String username;
    private Counsel counsel;

}
