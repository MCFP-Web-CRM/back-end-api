package com.mcfuturepartners.crm.api.sms.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.sms.dto.PhoneListDto;
import com.mcfuturepartners.crm.api.sms.dto.SmsDto;
import com.mcfuturepartners.crm.api.sms.dto.SmsResponseDto;
import com.mcfuturepartners.crm.api.util.sms.SmsRequestHandler;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsRequestHandler smsRequestHandler;
    @GetMapping("1")
    public ResponseEntity<PhoneListDto> getCategoryCustomerPhone(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                                 @RequestParam(name = "category-id") Long categoryId){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("2")
    public ResponseEntity<SmsResponseDto> getReservedSms(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                         Pageable pageable){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("3")
    public ResponseEntity<SmsResponseDto> getSmsWithoutReserved(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                                Pageable pageable){
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @PostMapping("4")
    @ApiOperation(value = "문자 메시지 전송 요청 api", notes = "문자 메시지 전송 요청 api")
    public ResponseEntity<String> sendRequestSms(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                 @RequestBody SmsDto smsDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(ObjectUtils.isEmpty(smsDto.getReservationTime())){
            //Reservation 저장

        } else{
            //Send 하고 저장
            smsRequestHandler.sendMessage(smsDto);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("5")
    public ResponseEntity<Void> deleteAllReservedSms(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("6")
    public ResponseEntity<Void> deleteSelectedSms(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
