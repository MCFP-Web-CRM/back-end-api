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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    public MessageDto saveMessage(MessageDto messageDto) {
        User user = userRepository.findByUsername(messageDto.getUsername()).orElseThrow(()-> new FindException("유저 정보 " + ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        try{
            Message message = messageDto.toEntity();
            message.setUser(user);
            message = messageRepository.save(message);
            MessageDto responseMessage = modelMapper.map(message,MessageDto.class);
            responseMessage.setUsername(message.getUser().getUsername());
            return responseMessage;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public Page<MessageDto> getSavedMessages(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username).orElseThrow(()-> new FindException("유저 정보 " + ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        Page<Message> messages = messageRepository.findMessagesByUser(user,pageable);


        return new PageImpl<>(messages.stream().map(message -> {
            MessageDto messageDto = modelMapper.map(message,MessageDto.class);
            messageDto.setUsername(message.getUser().getUsername());
            return messageDto;
        }).collect(Collectors.toList()),pageable,messages.getTotalElements());

    }

    @Override
    public void deleteSavedMessage(MessageDto messageDto) {

        try{
            messageRepository.deleteById(messageDto.getMessageId());
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }

    }

    @Override
    public MessageDto updateSavedMessage(MessageDto messageDto) {
        Message message = messageRepository.findById(messageDto.getMessageId()).orElseThrow(()-> new FindException(ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        message.updateModified(messageDto.toEntity());

        try{
            message = messageRepository.save(message);
            MessageDto responseMessage = modelMapper.map(message,MessageDto.class);
            responseMessage.setUsername(message.getUser().getUsername());
            return responseMessage;
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}
