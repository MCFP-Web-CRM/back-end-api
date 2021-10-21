package com.mcfuturepartners.crm.api.user.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RequestLogin {
    @NotNull(message = "username cannot be null")
    @Size(min = 2, message = "Username must be longer than 2 characters")
    private String username;

    @NotNull(message = "password cannot be null")
    @Size(min = 8, message = "Password must be equal or greater than 8 characters")
    private String password;
}
