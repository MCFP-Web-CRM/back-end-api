package com.mcfuturepartners.crm.api.message.service;

import com.mcfuturepartners.crm.api.message.dto.MessageDto;
import com.mcfuturepartners.crm.api.message.entity.Message;

import java.util.List;

public interface MessageService {
    List<MessageDto> saveMessage(MessageDto messageDto);
    List<MessageDto> getSavedMessages(String username);
    List<MessageDto> deleteSavedMessage(MessageDto messageDto);
    List<MessageDto> updateSavedMessage(MessageDto messageDto);
}
