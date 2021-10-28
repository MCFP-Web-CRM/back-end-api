package com.mcfuturepartners.crm.api.user.service;

import com.mcfuturepartners.crm.api.user.dto.UserDto;
import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import com.mcfuturepartners.crm.api.user.dto.UserRevenueDto;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.LoginException;
import com.mcfuturepartners.crm.api.user.entity.UserRevenue;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider provider;

    @Override
    public String signup(UserDto userDto) {
        User user = userDto.toEntity();
        if(!userRepository.existsByUsername(user.getUsername())){
            user.setPassword(encoder.encode(user.getPassword()));
            log.info(user.toString());

            userRepository.save(user);
            log.info(user.toString());

            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getUsername(),"",user.getAuthorities());
            return provider.createToken(usernamePasswordAuthenticationToken);
        }
        return ErrorCode.USER_ALREADY_EXISTS.getMsg();
    }
    @Override
    public String signin(User user) throws LoginException{
        try {
            String token = (encoder.matches(user.getPassword(), userRepository.findByUsername(user.getUsername()).get().getPassword()))
            ? provider.createToken(new UsernamePasswordAuthenticationToken(user.getUsername(),"",userRepository.findByUsername(user.getUsername()).get().getAuthorities()))
                    :"Wrong Password";
            return token;
        } catch (Exception e){
            throw new LoginException();
        }
    }

    @Override
    public String deleteUser(long id) {
        try {
            userRepository.delete(userRepository.findById(id).get());
            return "successfully done";
        } catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<UserRevenue> getAllUserRevenue() {
        return userRepository.findAll().stream().map(user -> UserRevenueDto.salesRevenue(user)).collect(Collectors.toList());
    }

    @Override
    public UserResponseDto getUserById(long id)  {
        User user = userRepository.findById(id).get();
        UserResponseDto userResponseDto = new UserResponseDto();
        userResponseDto.setName(user.getName());
        userResponseDto.setPhone(user.getPhone());
        userResponseDto.setId(user.getId());
        userResponseDto.setDepartmentName(departmentRepository.findById(user.getDepartment().getId()).get().getName());

        return userResponseDto;
    }
}
