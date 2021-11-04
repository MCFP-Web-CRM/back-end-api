package com.mcfuturepartners.crm.api.message.repository;

import com.mcfuturepartners.crm.api.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
