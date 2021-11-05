package com.mcfuturepartners.crm.api.schedule.service;

import com.mcfuturepartners.crm.api.customer.repository.CustomerRepositoryImpl;
import com.mcfuturepartners.crm.api.exception.AuthorizationException;
import com.mcfuturepartners.crm.api.exception.DatabaseErrorCode;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.schedule.dto.*;
import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import com.mcfuturepartners.crm.api.schedule.repository.ScheduleRepository;
import com.mcfuturepartners.crm.api.schedule.repository.ScheduleRepositoryImpl;
import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository scheduleRepository;
    private final ScheduleRepositoryImpl qScheduleRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<UserScheduleResponse> getAllSchedulesInBetween(ScheduleGet scheduleGet) {
        List<Schedule> calendarSchedule = qScheduleRepository.findAllByStartDateOrEndDateIsBetween(scheduleGet.getCalendarStartDate(),scheduleGet.getCalendarEndDate());
        List<User> userList = userRepository.findAll();
        List<UserScheduleResponse> availableSchedule = new ArrayList<>();

        availableSchedule.addAll(userList.stream().map(user -> UserScheduleResponse.builder()
                .user(modelMapper.map(user,UserResponseDto.class))
                .scheduleList(calendarSchedule.stream()
                        .filter(schedule -> schedule.getUser().getUsername().equals(user.getUsername()))
                        .map(schedule -> modelMapper.map(schedule,ScheduleResponse.class))
                        .collect(Collectors.toList())).build()).collect(Collectors.toList()));

        return availableSchedule;
    }

    @Override
    public List<UserScheduleResponse> getScheduleInBetween(ScheduleGet scheduleGet) {
        List<Schedule> calendarSchedule = qScheduleRepository.findAllByStartDateOrEndDateIsBetween(scheduleGet.getCalendarStartDate(),scheduleGet.getCalendarEndDate());
        List<User> userList = userRepository.findAll();
        List<UserScheduleResponse> availableSchedule = new ArrayList<>();

        userList.removeIf(user -> user.getUsername().equals(scheduleGet.getUsername()));

        availableSchedule.add(
                UserScheduleResponse.builder()
                        .user(modelMapper.map(userRepository.findByUsername(scheduleGet.getUsername()).orElseThrow(()->new FindException(DatabaseErrorCode.USER_NOT_FOUND.name())), UserResponseDto.class))
                        .scheduleList(calendarSchedule.stream()
                                .filter(schedule -> schedule.getUser().getUsername().equals(scheduleGet.getUsername()))
                                .map(schedule -> modelMapper.map(schedule,ScheduleResponse.class))
                                .collect(Collectors.toList()) ).build());

       availableSchedule.addAll(userList.stream().map(user -> UserScheduleResponse.builder()
                        .user(modelMapper.map(user,UserResponseDto.class))
                                .scheduleList(calendarSchedule.stream()
                                        .filter(schedule -> schedule.getUser().getUsername().equals(user.getUsername()))
                                        .filter(schedule -> schedule.getIsPublic())
                                        .map(schedule -> modelMapper.map(schedule,ScheduleResponse.class))
                                        .collect(Collectors.toList())).build()).collect(Collectors.toList()));

        return availableSchedule;
    }

    @Override
    public ScheduleResponse getSchedule(Long scheduleId, String username) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()->new FindException("스케줄 ID Not found"+ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        if(!schedule.getUser().getUsername().equals(username)){
            if(!schedule.getIsPublic()){
                throw new AuthorizationException("조회 권한 없음");
            }
        }
        return modelMapper.map(schedule,ScheduleResponse.class);
    }

    @Override
    public ScheduleResponse saveSchedule(ScheduleRegister scheduleRegister) {
        User user = userRepository.findByUsername(scheduleRegister.getUserName()).orElseThrow(()->new FindException(DatabaseErrorCode.USER_NOT_FOUND.name()));
        Schedule schedule = scheduleRegister.toEntity();
        schedule.setUser(user);
        return modelMapper.map(scheduleRepository.save(schedule),ScheduleResponse.class);
    }

    @Override
    public ScheduleResponse updateSchedule(Long scheduleId, ScheduleUpdate scheduleUpdate) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()->new FindException("스케줄 ID Not found"+ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        if(!schedule.getUser().getUsername().equals(scheduleUpdate.getUsername())){
            throw new AuthorizationException("수정 권한 없음");
        }

        return modelMapper.map(scheduleRepository.save(schedule.updateModified(scheduleUpdate)),ScheduleResponse.class);
    }

    @Override
    public void deleteSchedule(Long scheduleId, String username) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(()-> new FindException("스케줄 ID Not found"+ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        if(!schedule.getUser().getUsername().equals(username)){
            throw new AuthorizationException("삭제 권한 없음");
        }
        try{
            scheduleRepository.deleteById(scheduleId);
        }catch (Exception e){
            throw e;
        }

    }
}
