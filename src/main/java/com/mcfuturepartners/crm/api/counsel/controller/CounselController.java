package com.mcfuturepartners.crm.api.counsel.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.dto.CounselResponseDto;
import com.mcfuturepartners.crm.api.counsel.dto.CounselUpdateDto;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import com.mcfuturepartners.crm.api.exception.CounselException;
import com.mcfuturepartners.crm.api.counsel.service.CounselService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/counsel")
@RequiredArgsConstructor
@Slf4j
public class CounselController {
    private final CounselService counselService;
    private final CustomerService customerService;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @PostMapping
    @ApiOperation(value = "상담 저장", notes = "고객 상담 저장 api")
    public ResponseEntity<List<CounselResponseDto>> saveCounsel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken, @RequestBody CounselDto counselDto){
        String token = bearerToken.replace("Bearer ","");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        counselDto.setUsername(username);

        return new ResponseEntity<>(counselService.saveCounsel(counselDto),HttpStatus.CREATED);
    }

    //bearerToken 권한 확인하고, ADMIN 권한이면 전체 갖다주기. USER면 본인 해당 부분만 갖다주기
    @GetMapping
    @ApiOperation(value = "상담 내역 전체 조회", notes = "현재 customer 도메인으로 상담 내용 데이터를 받아서 사용하지 않음")
    public ResponseEntity<List<CounselResponseDto>> getCounsel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(counselService.findAll(), HttpStatus.OK);
        }
        return new ResponseEntity<>(counselService.findAllByUsername(username), HttpStatus.OK);
    }

    @GetMapping(path = "/user/{user-id}")
    @ApiOperation(value = "사원 번호 기반 상담 내역 조회", notes = "현재 customer 도메인으로 상담 내용 데이터를 받아서 사용하지 않음")
    public ResponseEntity<List<Counsel>> getCounselByUser(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,@PathVariable(value = "user-id") long userId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(counselService.findAllByUserId(userId),HttpStatus.OK);
        } else if(userRepository.getByUsername(username).getId() == userId){
            return new ResponseEntity<>(counselService.findAllByUserId(userId), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @GetMapping(path = "/{counsel-id}")
    @ApiOperation(value = "상담 번호 기반 상담 내역 조회", notes = "현재 customer 도메인으로 상담 내용 데이터를 받아서 사용하지 않음")
    public ResponseEntity<Counsel> getCounselById(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,@PathVariable(value = "counsel-id") long counselId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(counselService.findById(counselId).orElseThrow(),HttpStatus.OK);
        }
        return new ResponseEntity<>(counselService.findByUsernameId(username,counselId).orElseThrow(),HttpStatus.OK);
    }


    @PutMapping(path = "/{counsel-id}")
    @ApiOperation(value = "상담 수정 데이터", notes = "상담 수정 데이터. PathVariable = counsel-id, 사원 username 필수")
    public ResponseEntity<List<CounselResponseDto>> updateCounsel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                          @PathVariable(value = "counsel-id") long counselId,
                                                          @RequestBody CounselUpdateDto counselUpdateDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        counselUpdateDto.setUsername(username);

        if(counselService.findCustomerIfManager(counselId,username)||tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(counselService.updateCounsel(counselId, counselUpdateDto),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
    @DeleteMapping(path = "/{counsel-id}")
    @ApiOperation(value = "상담 삭제 데이터", notes = "상담 삭제 데이터. PathVariable = counsel-id, 사원 username 필수 / Response로 삭제 반영한 상담 데이터 제공")
    public ResponseEntity<List<CounselResponseDto>> deleteCounsel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                          @PathVariable(value = "counsel-id") long counselId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(counselService.findCustomerIfManager(counselId,username) || tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(counselService.deleteCounsel(counselId),HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
