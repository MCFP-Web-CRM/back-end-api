package com.mcfuturepartners.crm.api.sms.repository;

import com.mcfuturepartners.crm.api.sms.entity.Sms;

import java.time.LocalDateTime;
import java.util.List;

public interface CustomSmsRepository {
    List<Sms> findReservedSmsBeforeNow(LocalDateTime processTime);
}
