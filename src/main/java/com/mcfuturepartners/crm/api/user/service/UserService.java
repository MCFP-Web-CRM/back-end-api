package com.mcfuturepartners.crm.api.user.service;

import com.mcfuturepartners.crm.api.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public interface UserService {
    String signup(User user);
    String signin(User user);
    String deleteUser(long id);
}
