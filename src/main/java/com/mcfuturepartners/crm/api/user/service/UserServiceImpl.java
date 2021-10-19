package com.mcfuturepartners.crm.api.user.service;

import com.mcfuturepartners.crm.api.user.dto.UserDto;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.exception.ErrorCode;
import com.mcfuturepartners.crm.api.user.exception.LoginException;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
@Slf4j
@Service @RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final TokenProvider provider;
    private final ModelMapper modelMapper;

    @Override
    public String signup(User user) {
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
            userRepository.deleteById(id);
            return "successfully done";
        } catch (Exception e){
            throw e;
        }
    }
}
