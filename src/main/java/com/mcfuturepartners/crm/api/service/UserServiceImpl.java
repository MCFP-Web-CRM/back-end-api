package com.mcfuturepartners.crm.api.service;

import com.mcfuturepartners.crm.api.entity.Authority;
import com.mcfuturepartners.crm.api.entity.User;
import com.mcfuturepartners.crm.api.entity.UserDto;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

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
            Set<Authority> authorities = new HashSet<>();
            authorities.add(Authority.USER);
            authorities.add(Authority.ADMIN);
            user.setAuthorities(authorities);
            userRepository.save(user);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken
                    = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),user.getAuthorities());
            return provider.createToken(usernamePasswordAuthenticationToken);
        }
        return null;
    }

    @Override
    public String signin(User user) {
        try {
            String token = (encoder.matches(user.getPassword(), userRepository.findByUsername(user.getUsername()).get().getPassword()))
            ? provider.createToken(new UsernamePasswordAuthenticationToken(user.getUsername(),userRepository.findByUsername(user.getUsername()).get().getAuthorities()))
                    :"Wrong Password";
            return token;
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
