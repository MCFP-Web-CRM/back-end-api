package com.mcfuturepartners.crm.api.sms.service;

import com.mcfuturepartners.crm.api.customer.dto.CustomerSearch;
import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.sms.dto.*;
import com.mcfuturepartners.crm.api.sms.entity.Sms;
import com.mcfuturepartners.crm.api.sms.entity.SmsStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

public interface SmsService {
    List<Sms> saveAll(SmsProcessDto smsDto, ResponseEntity responseEntity);

    List<SmsProcessDto> findReservedSmsBeforeNow(LocalDateTime localDateTime);

    SmsProcessDto createSmsProcessDto(SmsDto smsDto);

    List<Sms> updateReservedSmsTo(SmsProcessDto smsProcessDto, ResponseEntity responseEntity);

    List<CategoryCustomerPhone> getCategoriesWithNumberOfCustomers(CustomerSearch customerSearch);

    PhoneListDto getCategoryCustomerPhone (Long categoryId, CustomerSearch customerSearch);

    List<SmsResponseDto> getReservedSms(String username, Pageable pageable);

    List<SmsResponseDto> getSmsWithoutReserved(String username, Pageable pageable);

    void deleteReservedSms(List<Long> smsIds,String username);

    void deleteSmsRecords(List<Long> smsIds,String username);
}
