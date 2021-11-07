package com.mcfuturepartners.crm.api.message.repository;

import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findMessagesByUser(User user, Pageable pageable);
}
