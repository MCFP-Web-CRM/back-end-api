package com.mcfuturepartners.crm.api.sms.repository;

import com.mcfuturepartners.crm.api.sms.entity.Sms;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsRepository extends JpaRepository<Sms, Long> {
}
