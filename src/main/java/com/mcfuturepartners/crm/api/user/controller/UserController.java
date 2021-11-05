package com.mcfuturepartners.crm.api.user.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import com.mcfuturepartners.crm.api.department.service.DepartmentService;
import com.mcfuturepartners.crm.api.exception.AuthorizationException;
import com.mcfuturepartners.crm.api.user.dto.*;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.user.entity.UserRevenue;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import com.mcfuturepartners.crm.api.security.filter.TokenFilter;
import com.mcfuturepartners.crm.api.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final UserService userService;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder encoder;
    private final ModelMapper mapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @GetMapping(path="/hello")
    @ApiOperation(value = "준형이 안녕", notes = "살려줘")
    public ResponseEntity<String> getHello(){
        return ResponseEntity.ok().body("Hello");
    }


    @PostMapping(path="/signin")
    @ApiOperation(value = "로그인 수행 api", notes = "로그인 수행 확인되면 Token과 권한 body에 태워서 보냄")
    public ResponseEntity<UserLoginResponseDto> signin(@RequestBody RequestLogin requestLogin){
        UserLoginResponseDto loginInfo = userService.signin(mapper.map(requestLogin, User.class));

        if(loginInfo.getToken() != "Wrong Password" && loginInfo.getToken() != null){
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.add(TokenFilter.AUTHORIZATION_HEADER, "Bearer "+loginInfo.getToken());

            return new ResponseEntity<>(loginInfo, httpHeaders, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping
    @ApiOperation(value = "전체 사원명 호출 api", notes = "전체 사원은 호출함")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return new ResponseEntity<>(userService.getAllUsers(),HttpStatus.OK);
    }

    @GetMapping(path="/{userid}")
    @ApiOperation(value = "개별 사원 호출 api", notes = "개별 사원 호출")
    public ResponseEntity<UserResponseDto> getUser(@PathVariable("userid") Long userId){
        return new ResponseEntity<>(userService.getUserById(userId),HttpStatus.OK);
    }

    @GetMapping(path = "/revenue")
    @ApiOperation(value = "전체 사원 매출 조회 api", notes = "이번달, 오늘 전체 사원 revenue")
    public ResponseEntity<List<UserRevenueResponseDto>> getUserRevenue(){
        return new ResponseEntity<>(userService.getAllUserRevenue(),HttpStatus.OK);
    }

    @PutMapping(path = "/{userid}")
    @ApiOperation(value = "",notes = "")
    public ResponseEntity<UserResponseDto> updateUser(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                      @PathVariable("userid") Long userId,
                                                      @RequestBody UserDto userDto){
        String token = bearerToken.replace("Bearer ","");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        UserResponseDto userResponseDto;

        userDto.setUsername(username);
        if(!StringUtils.hasText(userDto.getAuthority())){
            userDto.setAuthority(tokenProvider.getAuthentication(token).getAuthorities().toString());
        }

        try{
            userResponseDto = userService.updateUser(userId,userDto);
        }catch (AuthorizationException authorizationException){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(userResponseDto,HttpStatus.OK);
    }
}
