package com.mcfuturepartners.crm.api.sms.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SmsStatus {
    SENT("발신"),
    RESERVED("예약"),
    CANCEL("취소"),
    FAILED("실패");
    private final String msg;
}
