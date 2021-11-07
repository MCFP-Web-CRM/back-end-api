package com.mcfuturepartners.crm.api.message.service;

import com.mcfuturepartners.crm.api.message.dto.MessageDto;
import com.mcfuturepartners.crm.api.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {
    MessageDto saveMessage(MessageDto messageDto);
    Page<MessageDto> getSavedMessages(String username, Pageable pageable);
    void deleteSavedMessage(MessageDto messageDto);
    MessageDto updateSavedMessage(MessageDto messageDto);
}
