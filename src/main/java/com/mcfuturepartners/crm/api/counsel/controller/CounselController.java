package com.mcfuturepartners.crm.api.counsel.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.repository.CounselRepository;
import com.mcfuturepartners.crm.api.counsel.service.CounselService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/counsel")
@RequiredArgsConstructor
@Slf4j
public class CounselController {
    private final CounselService counselService;

    @GetMapping("/health-check")
    public ResponseEntity<String> counseHealthCheck(){
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

}
