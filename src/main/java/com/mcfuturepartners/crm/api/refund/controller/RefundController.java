package com.mcfuturepartners.crm.api.refund.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.exception.RefundException;
import com.mcfuturepartners.crm.api.order.service.OrderService;
import com.mcfuturepartners.crm.api.refund.dto.RefundDto;
import com.mcfuturepartners.crm.api.refund.service.RefundService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/refund")
@RequiredArgsConstructor
public class RefundController {
    private final TokenProvider tokenProvider;
    private final RefundService refundService;
    @PostMapping
    public ResponseEntity<Void> makeRefund(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                           @RequestBody RefundDto refundDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        if(!tokenProvider.getAuthentication(token).getAuthorities().toString().contains("ADMIN")){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        try{
            refundService.makeRefund(refundDto);
        } catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (RefundException refundException){
            refundException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
