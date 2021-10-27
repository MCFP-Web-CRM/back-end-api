package com.mcfuturepartners.crm.api.user.controller;

import com.mcfuturepartners.crm.api.user.dto.RequestLogin;
import com.mcfuturepartners.crm.api.user.dto.UserDto;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.user.entity.UserRevenue;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import com.mcfuturepartners.crm.api.security.filter.TokenFilter;
import com.mcfuturepartners.crm.api.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
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
        if(userService.signup(userDto).equals(ErrorCode.USER_ALREADY_EXISTS.getMsg())){
            return ResponseEntity.badRequest().body(ErrorCode.USER_ALREADY_EXISTS.getMsg());
        }
        return new ResponseEntity<>("User Register Succeeded", HttpStatus.CREATED);
    }

    @PostMapping(path="/signin")
    public ResponseEntity<String> signin(@RequestBody RequestLogin requestLogin){
        String jwt = userService.signin(mapper.map(requestLogin, User.class));

        if(jwt != "Wrong Password" && jwt != null){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(TokenFilter.AUTHORIZATION_HEADER, "Bearer "+jwt);
            return new ResponseEntity<>("Logged in", httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>("Log in failed",HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping(path = "/{userid}")
    public ResponseEntity<String> deleteUser(@PathVariable("userid") long userId){
        if(userService.deleteUser(userId).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }

    @GetMapping(path = "/revenue")
    public ResponseEntity<List<UserRevenue>> getUserRevenue(){
        return new ResponseEntity<>(userService.getAllUserRevenue(),HttpStatus.OK);
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
