package com.mcfuturepartners.crm.api.user.service;

import com.mcfuturepartners.crm.api.department.dto.DepartmentResponseDto;
import com.mcfuturepartners.crm.api.exception.*;
import com.mcfuturepartners.crm.api.order.entity.Order;
import com.mcfuturepartners.crm.api.order.repository.OrderRepository;
import com.mcfuturepartners.crm.api.user.dto.*;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.UserRevenue;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final TokenProvider provider;

    @Override
    public UserResponseDto signup(UserDto userDto) {
        User user = userDto.toEntity();
        if(!ObjectUtils.isEmpty(userDto.getDepartmentId())){
            user.setDepartment(departmentRepository.findById(userDto.getDepartmentId()).orElseThrow(()->new FindException("DEPARTMENT "+ErrorCode.RESOURCE_NOT_FOUND)));
        }

        if(!userRepository.existsByUsername(user.getUsername())){
            user.setPassword(encoder.encode(user.getPassword()));
            try{
                user = userRepository.save(user);

            }catch (Exception e){
                throw e;
            }

            return modelMapper.map(user,UserResponseDto.class);

        }
        throw new LoginException(ErrorCode.USER_ALREADY_EXISTS.getMsg());
    }
    @Override
    public UserResponseDto updateUser(Long userId,UserUpdateDto userUpdateDto) {
        User user = userRepository.findById(userId).orElseThrow(()->new FindException("USER "+ErrorCode.RESOURCE_NOT_FOUND));

        //if not admin or user itself, unauthorized error
        if(!userUpdateDto.getUsername().equals(user.getUsername())){
            throw new AuthorizationException("UNAUTHORIZED");
        }
        User modifiedUser = userUpdateDto.toEntity();

        //changing password
        if(StringUtils.hasText(modifiedUser.getPassword())){
            modifiedUser.setPassword(encoder.encode(modifiedUser.getPassword()));
        }

        try{
            user = userRepository.save(user.updateModified(modifiedUser));
        } catch (Exception e){
            throw e;
        }
        UserResponseDto userResponseDto = modelMapper.map(user,UserResponseDto.class);

        if(!ObjectUtils.isEmpty(user.getDepartment())){
            userResponseDto.setDepartment(modelMapper.map(user.getDepartment(),DepartmentResponseDto.class));
        }
        return userResponseDto;
    }

    @Override
    public UserResponseDto updateUserByAdmin(Long userId, AdminUserUpdateDto adminUserUpdateDto) {
        User user = userRepository.findById(userId).orElseThrow(()->new FindException("USER "+ErrorCode.RESOURCE_NOT_FOUND));

        User modifiedUser = adminUserUpdateDto.toEntity();
        
        //if new department
        if(!ObjectUtils.isEmpty(adminUserUpdateDto.getDepartmentId())){
            modifiedUser.setDepartment(departmentRepository.findById(adminUserUpdateDto.getDepartmentId()).orElseThrow(()-> new FindException("Department "+ErrorCode.RESOURCE_NOT_FOUND)));
        }

        try{
            user = userRepository.save(user.updateModified(modifiedUser));
        } catch (Exception e){
            throw e;
        }
        UserResponseDto userResponseDto = modelMapper.map(user,UserResponseDto.class);

        if(!ObjectUtils.isEmpty(user.getDepartment())){
            userResponseDto.setDepartment(modelMapper.map(user.getDepartment(),DepartmentResponseDto.class));
        }
        return userResponseDto;
    }

    @Override
    public User getUserByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    @Override
    public UserRevenueRefundDto getMonthlySalesRefundProfit(Long managerId) {
        User manager = userRepository.findById(managerId).orElseThrow(()->new FindException("USER "+ErrorCode.RESOURCE_NOT_FOUND));
        LocalDateTime startOfMonth = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonthValue(), 1).atStartOfDay();
        Long salesAmount = manager.getOrders().stream()
                .filter(order -> order.getRegDate().isAfter(startOfMonth))
                .map(order -> order.getPrice())
                .reduce(0L,Long::sum);

        Long refundAmount = manager.getOrders().stream()
                .filter(order -> !ObjectUtils.isEmpty(order.getRefund()))
                .map(order -> order.getRefund())
                .filter(refund -> !ObjectUtils.isEmpty(refund.getRegDate()))
                .filter(refund -> refund.getRegDate().isAfter(startOfMonth))
                .map(refund -> refund.getRefundAmount())
                .reduce(0L,Long::sum);

        return UserRevenueRefundDto.builder()
                .salesAmount(salesAmount)
                .refundAmount(refundAmount)
                .profitAmount(salesAmount-refundAmount)
                .build();
    }

    @Override
    public UserLoginResponseDto signin(User user) throws LoginException{
        UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto();
        User loginUser = userRepository.findByUsername(user.getUsername()).get();
        try {
            userLoginResponseDto.setToken((encoder.matches(user.getPassword(), loginUser.getPassword()))
            ? provider.createToken(new UsernamePasswordAuthenticationToken(user.getUsername(),"", loginUser.getAuthorities()))
                    :"Wrong Password");
            if(loginUser.getAuthorities().contains(Authority.ADMIN)) userLoginResponseDto.setAuthority("ADMIN");
            else userLoginResponseDto.setAuthority("USER");
            userLoginResponseDto.setId(loginUser.getId());
            return userLoginResponseDto;
        } catch (Exception e){
            throw new LoginException();
        }
    }

    @Override
    public String deleteUser(long id) {
        User user = userRepository.findById(id).orElseThrow(()-> new FindException("USER "+ErrorCode.RESOURCE_NOT_FOUND));
        //user.eraseAllSchedules();
        user.eraseConnectionFromUser();
        try {
            userRepository.delete(user);
            return "successfully done";
        } catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll().stream().map(user -> {
            UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
            if(user.getAuthorities().contains(Authority.ADMIN)){
                userResponseDto.setAuthority("ADMIN");
            }
            else{
                userResponseDto.setAuthority("USER");
            }
            if(!ObjectUtils.isEmpty(user.getDepartment())){
                userResponseDto.setDepartment(modelMapper.map(user.getDepartment(), DepartmentResponseDto.class));
            }
            return userResponseDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserRevenueResponseDto> getAllUserRevenue() {

        List<User> userList = userRepository.findAll();
        LocalDateTime startOfMonth = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonthValue(), 1).atStartOfDay();
        LocalDateTime startOfDay = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonthValue(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getDayOfMonth()).atStartOfDay();

        List<UserRevenueResponseDto> userMonthlyDailyRevenueList = new ArrayList<>();

        userMonthlyDailyRevenueList.add(UserRevenueResponseDto.builder().unit("monthlyRevenue").userRevenueList(
                userList.stream().map(user ->
                UserRevenueDto.builder()
                        .user(modelMapper.map(user,UserResponseDto.class))
                        .amount(user.getTotalRevenueAfter(startOfMonth))
                        .build()
        ).sorted((o1, o2) -> (int) (o2.getAmount() - o1.getAmount())).collect(Collectors.toList())).build());

        userMonthlyDailyRevenueList.add(UserRevenueResponseDto.builder().unit("dailyRevenue").userRevenueList(
                userList.stream().map(user ->
                        UserRevenueDto.builder()
                                .user(modelMapper.map(user,UserResponseDto.class))
                                .amount(user.getTotalRevenueAfter(startOfDay))
                                .build()
                ).sorted((o1, o2) -> (int) (o2.getAmount() - o1.getAmount())).collect(Collectors.toList())).build());

        return userMonthlyDailyRevenueList;
    }

    @Override
    public UserResponseDto getUserById(long id)  {
        return userRepository.findById(id).map(user -> {
            UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
            if(!ObjectUtils.isEmpty(user.getDepartment())){
                userResponseDto.setDepartment(modelMapper.map(user.getDepartment(), DepartmentResponseDto.class));
            }
            return userResponseDto;
        }).orElseThrow(()-> new FindException(DatabaseErrorCode.USER_NOT_FOUND.name()));
    }


}
