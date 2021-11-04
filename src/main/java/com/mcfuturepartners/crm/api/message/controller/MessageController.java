package com.mcfuturepartners.crm.api.message.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.message.dto.MessageDto;
import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.message.entity.SmsType;
import com.mcfuturepartners.crm.api.message.service.MessageService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.DataOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/SMS")
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageService messageService;

    @GetMapping("/message")
    public ResponseEntity<List<MessageDto>> getSavedMessages(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        List<MessageDto> messages ;

        try{
            messages = messageService.getSavedMessages(username);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(messages,HttpStatus.OK);
    }

    @PostMapping(value = "/message")
    public ResponseEntity<List<MessageDto>> saveMessage(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                     @RequestBody MessageDto messageDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        messageDto.setUsername(username);
        List<MessageDto> messages;

        try{
            messages = messageService.saveMessage(messageDto);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<List<MessageDto>> updateMessage(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                       @RequestBody MessageDto messageDto) {
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        messageDto.setUsername(username);
        List<MessageDto> messages;
        try{
            messages = messageService.updateSavedMessage(messageDto);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    @DeleteMapping(path = "{message-id}")
    public ResponseEntity<List<MessageDto>> deleteMessage(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                       @PathVariable(value = "message-id") Long messageId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        List<MessageDto> messages;

        try {
            messages = messageService.deleteSavedMessage(MessageDto.builder().messageId(messageId).username(username).build());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(messages, HttpStatus.NO_CONTENT);
    }

}

