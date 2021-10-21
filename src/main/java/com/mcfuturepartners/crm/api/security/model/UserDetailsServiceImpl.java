package com.mcfuturepartners.crm.api.security.model;

import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(username))
                .orElseThrow(() -> new UsernameNotFoundException(username + "이란 아이디는 존재하지 않습니다."));

        return UserDetailsImpl.build(user.get());
    }
}
