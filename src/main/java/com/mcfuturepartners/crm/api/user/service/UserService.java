package com.mcfuturepartners.crm.api.user.service;

import com.mcfuturepartners.crm.api.user.dto.*;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.entity.UserRevenue;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserService {
    UserResponseDto signup(UserDto userDto);
    UserLoginResponseDto signin(User user);
    String deleteUser(long id);
    List<UserResponseDto> getAllUsers();
    List<UserRevenueResponseDto> getAllUserRevenue();
    UserResponseDto getUserById(long id);
    User getUserByName(String name);
    UserResponseDto updateUser(Long userId, UserUpdateDto userDto);
    UserResponseDto updateUserByAdmin(Long userId, AdminUserUpdateDto userDto);
    UserRevenueRefundDto getMonthlySalesRefundProfit(Long managerId);
}
