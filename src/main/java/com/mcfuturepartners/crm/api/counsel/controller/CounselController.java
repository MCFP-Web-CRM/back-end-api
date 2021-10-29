package com.mcfuturepartners.crm.api.counsel.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
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

    @GetMapping("/health-check")
    public ResponseEntity<String> counselHealthCheck(){
        return new ResponseEntity<>("counsel Health Check", HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "상담 저장", notes = "고객 상담 저장 api")
    public ResponseEntity<List<CounselDto>> saveCounsel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken, @RequestBody CounselDto counselDto){
        String token = bearerToken.replace("Bearer ","");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        counselDto.setUsername(username);

        return new ResponseEntity<>(counselService.saveCounsel(counselDto),HttpStatus.CREATED);
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

    @PutMapping(path = "/{counsel-id}")
    public ResponseEntity<List<CounselDto>> updateCounsel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken, @PathVariable(value = "counsel-id") long counselId,
                                                @RequestBody CounselDto counselDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        counselDto.setUsername(username);

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){

            return new ResponseEntity<>(counselService.updateCounsel(counselId, counselDto),HttpStatus.OK);
        }

        if(counselService.updateCounsel(counselId, counselDto).equals("UNAUTHORIZED")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(counselService.updateCounsel(counselId, counselDto),HttpStatus.OK);

    }
    @DeleteMapping(path = "/{counsel-id}")
    public ResponseEntity<List<CounselDto>> deleteCounsel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                          @PathVariable(value = "counsel-id") long counselId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(counselService.deleteCounsel(counselId),HttpStatus.NO_CONTENT);
        }
        try{
            return new ResponseEntity<>(counselService.deleteCounselByUsername(username,counselId),HttpStatus.NO_CONTENT);
        }catch(CounselException c){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
