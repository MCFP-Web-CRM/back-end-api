package com.mcfuturepartners.crm.api.user.service;

import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.entity.UserRevenue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    String signup(User user);
    String signin(User user);
    String deleteUser(long id);
    List<UserRevenue> getAllUserRevenue();
}
