package com.mcfuturepartners.crm.api;

import com.mcfuturepartners.crm.api.entity.User;
import com.mcfuturepartners.crm.api.entity.UserDto;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.repository.UserRepository;
import com.mcfuturepartners.crm.api.security.filter.TokenFilter;
import com.mcfuturepartners.crm.api.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class TestController {
    private final UserRepository userRepository;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping(path="/hello")
    public ResponseEntity<String> getHello(){
        return ResponseEntity.ok().body("Hello");
    }

    @PostMapping(path="/signup")
    public ResponseEntity<String> signup(@RequestBody UserDto userDto){
        return ResponseEntity.ok(userService.signup(mapper.map(userDto, User.class)));
    }
    @PostMapping(path="/signin")
    public ResponseEntity<String> signin(@RequestBody UserDto userDto){
        String jwt = userService.signin(mapper.map(userDto,User.class));
        if(jwt != "Wrong Password" && jwt != null){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(TokenFilter.AUTHORIZATION_HEADER, "Bearer "+jwt);
            return new ResponseEntity<>("Logged in", httpHeaders, HttpStatus.OK);
        }
        return ResponseEntity.ok("Log in failed");

    }
    @PostMapping(path="/authenticate")
    public ResponseEntity<String> authorize(@RequestBody UserDto userDto){
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDto.getUsername(),encoder.encode(userDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        String jwt = tokenProvider.createToken(authenticationToken);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(TokenFilter.AUTHORIZATION_HEADER, "Bearer "+jwt);

        return new ResponseEntity<>("HIHI", httpHeaders, HttpStatus.OK);
    }
    @GetMapping("/admin")
    public ResponseEntity<String> forAdmin(){
        return ResponseEntity.ok().body("admin 만세!");
    }
    @GetMapping("/user")
    public ResponseEntity<String> forUser(){
        return ResponseEntity.ok().body("user 만세!");
    }
}
