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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder encoder;
    private final TokenProvider provider;

    @Override
    public String signup(UserDto userDto) {
        User user = userDto.toEntity();
        if(!userRepository.existsByUsername(user.getUsername())){
            user.setPassword(encoder.encode(user.getPassword()));

            userRepository.save(user);

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getUsername(),"",user.getAuthorities());
            return provider.createToken(usernamePasswordAuthenticationToken);
        }
        return ErrorCode.USER_ALREADY_EXISTS.getMsg();
    }
    @Override
    public UserResponseDto updateUser(Long userId,UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(()->new FindException("USER "+ErrorCode.RESOURCE_NOT_FOUND));

        //if not admin or user itself, unauthorized error
        if(!userDto.getAuthority().contains("ADMIN")&&!userDto.getUsername().equals(user.getUsername())){
            throw new AuthorizationException("UNAUTHORIZED");
        }
        User modifiedUser = userDto.toEntity();

        //changing password
        if(StringUtils.hasText(modifiedUser.getPassword())){
            modifiedUser.setPassword(encoder.encode(modifiedUser.getPassword()));
        }

        //if new department
        if(!ObjectUtils.isEmpty(userDto.getDepartmentId())){
            modifiedUser.setDepartment(departmentRepository.findById(userDto.getDepartmentId()).orElseThrow(()-> new FindException("Department "+ErrorCode.RESOURCE_NOT_FOUND)));
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
    public UserLoginResponseDto signin(User user) throws LoginException{
        UserLoginResponseDto userLoginResponseDto = new UserLoginResponseDto();
        User loginUser = userRepository.findByUsername(user.getUsername()).get();
        try {
            userLoginResponseDto.setToken((encoder.matches(user.getPassword(), loginUser.getPassword()))
            ? provider.createToken(new UsernamePasswordAuthenticationToken(user.getUsername(),"", loginUser.getAuthorities()))
                    :"Wrong Password");
            if(loginUser.getAuthorities().contains(Authority.ADMIN)) userLoginResponseDto.setAuthority("ADMIN");
            else userLoginResponseDto.setAuthority("USER");
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
            userResponseDto.setDepartment(modelMapper.map(user.getDepartment(), DepartmentResponseDto.class));
            return userResponseDto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserRevenueResponseDto> getAllUserRevenue() {

        List<User> userList = userRepository.findAll();
        LocalDateTime startOfMonth = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), 1).atStartOfDay();
        LocalDateTime startOfDay = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue(), LocalDateTime.now().getDayOfMonth()).atStartOfDay();

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
            userResponseDto.setDepartment(modelMapper.map(user.getDepartment(), DepartmentResponseDto.class));
            return userResponseDto;
        }).orElseThrow(()-> new FindException(DatabaseErrorCode.USER_NOT_FOUND.name()));
    }


}
