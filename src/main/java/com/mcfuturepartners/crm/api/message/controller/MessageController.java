package com.mcfuturepartners.crm.api.message.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.message.dto.MessageDto;
import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.message.entity.SmsType;
import com.mcfuturepartners.crm.api.message.service.MessageService;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
@RequestMapping("/message")
@RequiredArgsConstructor
@Slf4j
public class MessageController {
    private final MessageService messageService;

    @GetMapping
    @ApiOperation(value = "저장된 메시지 호출 api", notes = "사용자가 저장한 메시지를 호출해주는 api")
    public ResponseEntity<Page<MessageDto>> getSavedMessages(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                             Pageable pageable){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        Page<MessageDto> messages ;

        try{
            messages = messageService.getSavedMessages(username,pageable);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(messages,HttpStatus.OK);
    }

    @PostMapping
    @ApiOperation(value = "문자 메시지 내용 저장 api", notes = "사용자가 작성한 메시지를 저장해주는 api")
    public ResponseEntity<MessageDto> saveMessage(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                     @RequestBody MessageDto messageDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        messageDto.setUsername(username);
        MessageDto message;

        try{
            message = messageService.saveMessage(messageDto);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @PutMapping(path = "/{message-id}")
    @ApiOperation(value = "문자 메시지 내용 수정 api", notes = "문자 메시지 내용 수정 api")
    public ResponseEntity<MessageDto> updateMessage(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                       @RequestBody MessageDto messageDto,
                                                          @PathVariable(name = "message-id") Long messageId) {
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        messageDto.setUsername(username);
        messageDto.setMessageId(messageId);

        MessageDto message;
        try{
            message = messageService.updateSavedMessage(messageDto);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{message-id}")
    @ApiOperation(value = "문자 메시지 내용 삭제 api", notes = "문자 메시지 내용 삭제 api")
    public ResponseEntity<Void> deleteMessage(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                       @PathVariable(value = "message-id") Long messageId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        try {
            messageService.deleteSavedMessage(MessageDto.builder().messageId(messageId).username(username).build());
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

