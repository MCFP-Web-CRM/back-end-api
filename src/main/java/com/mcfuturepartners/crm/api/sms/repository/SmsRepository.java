package com.mcfuturepartners.crm.api.sms.repository;

import com.mcfuturepartners.crm.api.sms.entity.Sms;
import com.mcfuturepartners.crm.api.sms.entity.SmsStatus;
import com.mcfuturepartners.crm.api.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SmsRepository extends JpaRepository<Sms, Long> {
    List<Sms> findAllBySenderAndSmsStatus(User sender, SmsStatus smsStatus,  Pageable pageable);
    List<Sms> findAllBySenderAndSmsStatus(User sender, SmsStatus smsStatus);
    List<Sms> findAllBySenderAndSmsStatusIsNot(User sender, SmsStatus smsStatus,  Pageable pageable);
    List<Sms> findAllBySenderAndSmsStatusIsNot(User sender, SmsStatus smsStatus);
}
