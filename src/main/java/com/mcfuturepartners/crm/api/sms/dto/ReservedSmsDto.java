package com.mcfuturepartners.crm.api.sms.dto;

import org.springframework.http.ResponseEntity;

import java.util.List;

public class ReservedSmsDto {
    private List<Long> smsIds;
    private ResponseEntity responseEntity;
}
