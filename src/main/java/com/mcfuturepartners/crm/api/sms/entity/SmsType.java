package com.mcfuturepartners.crm.api.sms.entity;

import lombok.Getter;

@Getter
public enum SmsType {
    SMS("SMS"), LMS("LMS");
    private final String code;
    SmsType(String sms) {
        code=sms;
    }
}
