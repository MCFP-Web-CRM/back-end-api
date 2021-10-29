package com.mcfuturepartners.crm.api.sms.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
class SMSControllerTest {
    @Autowired
    SMSController smsController;

    @Test
    void send(){
        //given
        List<String> phone= new ArrayList<>();
        phone.add("01023367918");
        String content = "테스트문자입니다.";
        //when
        ResponseEntity responseEntity = smsController.sendMessage(phone, content);

        //then
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

    }
}