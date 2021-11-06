package com.mcfuturepartners.crm.api.sms.service;

import com.mcfuturepartners.crm.api.sms.entity.Sms;

import java.util.List;

public interface SmsService {
    List<Sms> saveAll(List<Sms> requestSms);
}
