package com.mcfuturepartners.crm.api.user.service;

import com.mcfuturepartners.crm.api.user.dto.UserDto;
import com.mcfuturepartners.crm.api.user.dto.UserLoginResponseDto;
import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.entity.UserRevenue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserService {
    String signup(UserDto userDto);
    UserLoginResponseDto signin(User user);
    String deleteUser(long id);
    List<UserResponseDto> getAllUsers();
    List<UserRevenue> getAllUserRevenue();
    UserResponseDto getUserById(long id);
}
