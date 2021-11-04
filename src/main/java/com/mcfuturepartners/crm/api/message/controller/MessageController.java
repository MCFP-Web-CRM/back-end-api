package com.mcfuturepartners.crm.api.message.controller;

import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.message.entity.SmsType;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
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
    @GetMapping("/message")
    public ResponseEntity<List<Message>> getSavedMessages(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(value = "/message")
    public ResponseEntity<List<Message>> saveMessage(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<List<Message>> updateMessage(){
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(path = "{sms-id}")
    public ResponseEntity<List<Message>> deleteMessage(@PathVariable(value = "sms-id") Long smsId){
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}

