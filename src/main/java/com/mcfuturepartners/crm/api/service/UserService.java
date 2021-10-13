package com.mcfuturepartners.crm.api.service;

import com.mcfuturepartners.crm.api.entity.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
    String signup(User user);
    String signin(User user);
}
