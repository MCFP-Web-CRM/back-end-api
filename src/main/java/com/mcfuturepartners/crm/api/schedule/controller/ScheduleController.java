package com.mcfuturepartners.crm.api.schedule.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.schedule.dto.*;
import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import com.mcfuturepartners.crm.api.schedule.repository.ScheduleRepository;
import com.mcfuturepartners.crm.api.schedule.service.ScheduleService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(value = "/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;

    @PostMapping
    @ApiOperation(value = "스케줄 추가 api", notes = "제목, 시작시간, 끝나는시간, 공개여부 필수 입력")
    public ResponseEntity<ScheduleResponse> addSchedule(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                        @RequestBody ScheduleRegister scheduleRegister){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        scheduleRegister.setUserName(username);
        try{
            scheduleService.saveSchedule(scheduleRegister);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<UserScheduleResponse>> getSchedule(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                                  @RequestParam("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                  @RequestParam("end-date")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        List<UserScheduleResponse> userScheduleResponseList;
        try{
            userScheduleResponseList = scheduleService.getScheduleInBetween(ScheduleGet.builder().calendarStartDate(startDate.atStartOfDay()).calendarEndDate(endDate.atTime(23,59,59,99)).username(username).build());
        } catch (FindException findException){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(userScheduleResponseList, HttpStatus.OK);
    }

    @PutMapping(value = "/{schedule-id}")
    public ResponseEntity<ScheduleResponse> updateSchedule(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                           @PathVariable(value = "schedule-id") Long scheduleId,
                                                           @RequestBody ScheduleUpdate scheduleUpdate){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        ScheduleResponse scheduleResponse ;
        try{
            scheduleResponse = scheduleService.updateSchedule(scheduleId,scheduleUpdate);
        } catch (FindException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(scheduleResponse,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{schedule-id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable(value = "schedule-id") Long scheduleId){
        try{
            scheduleService.deleteSchedule(scheduleId);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
