package com.mcfuturepartners.crm.api.counsel.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.exception.CounselException;
import com.mcfuturepartners.crm.api.counsel.service.CounselService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;

    @GetMapping("/health-check")
    public ResponseEntity<String> counselHealthCheck(){
        return new ResponseEntity<>("counsel Health Check", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> saveCounsel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken, @RequestBody CounselDto counselDto){
        String token = bearerToken.replace("Bearer ","");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        log.info(username, token);
        //userId validation check 하고 들어와야함
        counselService.saveCounsel(username,counselDto);
        return new ResponseEntity<>("save Succeeded",HttpStatus.CREATED);
    }

    //bearerToken 권한 확인하고, ADMIN 권한이면 전체 갖다주기. USER면 본인 해당 부분만 갖다주기
    @GetMapping
    public ResponseEntity<List<Counsel>> getCounsel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(counselService.findAll(), HttpStatus.OK);
        }
        return new ResponseEntity<>(counselService.findAllByUsername(username), HttpStatus.OK);
    }

    //getCounselByUser => ADMIN 권한이면 모두다 access 가능. USER 권한이면 요청이 본인 데이터 외에 요청 시 access deny
    @GetMapping(path = "/user/{user-id}")
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

    //getCounselById => ADMIN 권한이면 모두다 access 가능. USER 권한이면 본인 데이터 일 시에만 확인 가능 or access deny
    @GetMapping(path = "/{counsel-id}")
    public ResponseEntity<Counsel> getCounselById(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,@PathVariable(value = "counsel-id") long counselId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(counselService.findById(counselId).orElseThrow(),HttpStatus.OK);
        }
        return new ResponseEntity<>(counselService.findByUsernameId(username,counselId).orElseThrow(),HttpStatus.OK);
    }

    //searchByKeyword => ADMIN 권한이면 전체에서, USER 권한이면 본인 데이터 안에서
    @GetMapping(path="/search")
    public ResponseEntity<List<Counsel>> getCounselByKeyword(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,@RequestParam(value="keyword") String keyword){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            //findAllCounselByKeyword  and Response
            return new ResponseEntity<>(counselService.findAllByKeyword(keyword),HttpStatus.OK);
        }

        try{
            return new ResponseEntity<>(counselService.findAllByUsernameKeyword(username,keyword),HttpStatus.OK);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    //updateCounsel => path variable {id} ADMIN 권한이면 모두다 access 가능. USER 권한이면 본인 데이터 외 access deny
    @PutMapping(path = "/{counsel-id}")
    public ResponseEntity<Counsel> updateCounsel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken, @PathVariable(value = "counsel-id") long counselId,
                                                @RequestBody CounselDto counselDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(counselService.updateCounsel(counselId, counselDto), HttpStatus.OK);
        } else if(username.equals(counselDto.getUser().getUsername())){
            return new ResponseEntity<>(counselService.updateCounsel(counselId, counselDto), HttpStatus.OK);
        }
        //existByUsername and Id => no? 403(not authorized) Response dto layer에서 작업 후 controller에서 전달만
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    //deleteCounsel => path variable {id} ADMIN 권한이면 모두다 access 가능. USER 권한이면 본인 데이터 외 access deny
    @DeleteMapping(path = "/{counsel-id}")
    public ResponseEntity<Void> deleteCounsel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken, @PathVariable(value = "counsel-id") long counselId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            //delete and Response
            counselService.deleteCounsel(counselId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        try{
            counselService.deleteCounselByUsername(username,counselId);
        }catch(CounselException c){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
