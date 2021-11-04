package com.mcfuturepartners.crm.api.message.service;

import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.message.dto.MessageDto;
import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.message.repository.MessageRepository;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService{
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<MessageDto> saveMessage(MessageDto messageDto) {
        User user = userRepository.findByUsername(messageDto.getUsername()).orElseThrow(()-> new FindException("유저 정보 " + ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        try{
            Message message = messageDto.toEntity();
            message.setUser(user);
            messageRepository.save(message);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

        return getSavedMessages(user.getUsername());
    }

    @Override
    public List<MessageDto> getSavedMessages(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new FindException("유저 정보 " + ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        return user.getMessages().stream().map(message -> modelMapper.map(message,MessageDto.class)).collect(Collectors.toList());

    }

    @Override
    public List<MessageDto> deleteSavedMessage(MessageDto messageDto) {

        try{
            messageRepository.deleteById(messageDto.getMessageId());
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }

        return getSavedMessages(messageDto.getUsername());
    }

    @Override
    public List<MessageDto> updateSavedMessage(MessageDto messageDto) {
        Message message = messageRepository.findById(messageDto.getMessageId()).orElseThrow(()-> new FindException(ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        message.updateModified(messageDto.toEntity());

        try{
            messageRepository.save(message);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return getSavedMessages(messageDto.getUsername());
    }
}
