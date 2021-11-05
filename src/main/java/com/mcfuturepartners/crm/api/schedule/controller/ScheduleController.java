package com.mcfuturepartners.crm.api.schedule.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.exception.AuthorizationException;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.schedule.dto.*;
import com.mcfuturepartners.crm.api.schedule.entity.Schedule;
import com.mcfuturepartners.crm.api.schedule.repository.ScheduleRepository;
import com.mcfuturepartners.crm.api.schedule.service.ScheduleService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.Authority;
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
    private final TokenProvider tokenProvider;
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
    @ApiOperation(value = "스케줄 조회 api", notes = "시작, 끝 날짜 필수 입력")
    public ResponseEntity<List<UserScheduleResponse>> getSchedule(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                                  @RequestParam("start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                  @RequestParam("end-date")  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        List<UserScheduleResponse> userScheduleResponseList;

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return  new ResponseEntity<>(scheduleService.getAllSchedulesInBetween(ScheduleGet.builder().calendarStartDate(startDate.atStartOfDay()).calendarEndDate(endDate.atTime(23,59,59,99)).username(username).build()),HttpStatus.OK);
        }

        try{
            userScheduleResponseList = scheduleService.getScheduleInBetween(ScheduleGet.builder().calendarStartDate(startDate.atStartOfDay()).calendarEndDate(endDate.atTime(23,59,59,99)).username(username).build());
        } catch (FindException findException){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(userScheduleResponseList, HttpStatus.OK);
    }

    @GetMapping(path = "/{schedule-id}")
    @ApiOperation(value = "스케줄 상세 조회 api", notes = "schedule-id uri 포함 필수, admin 계정은 모두다 열람 가능한 부분 추가 구현 필요")
    public ResponseEntity<ScheduleResponse> getSpecificSchedule(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                                  @PathVariable(name = "schedule-id") Long scheduleId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        ScheduleResponse scheduleResponse;
        String authority = tokenProvider.getAuthentication(token).getAuthorities().toString();
        try{
            scheduleResponse = scheduleService.getSchedule(scheduleId,username,authority);

        } catch (AuthorizationException authorizationException){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        catch (FindException findException){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(scheduleResponse, HttpStatus.OK);
    }
    @PutMapping(value = "/{schedule-id}")
    @ApiOperation(value = "스케줄 수정 api", notes = "schedule-id uri 포함 필수, 등록한 계정 외에 admin 계정이어도 수정 불가")
    public ResponseEntity<ScheduleResponse> updateSchedule(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                           @PathVariable(value = "schedule-id") Long scheduleId,
                                                           @RequestBody ScheduleUpdate scheduleUpdate){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        ScheduleResponse scheduleResponse;

        scheduleUpdate.setSchedulId(scheduleId);
        scheduleUpdate.setUsername(username);

        try{
            scheduleResponse = scheduleService.updateSchedule(scheduleId,scheduleUpdate);

        } catch (AuthorizationException authorizationException){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }catch (FindException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(scheduleResponse,HttpStatus.OK);
    }

    @DeleteMapping(value = "/{schedule-id}")
    @ApiOperation(value = "스케줄 삭제 api", notes = "schedule-id uri 포함 필수, 등록한 계정 외에 admin 계정이어도 삭제 불가")
    public ResponseEntity<Void> deleteSchedule(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                               @PathVariable(value = "schedule-id") Long scheduleId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        try{
            scheduleService.deleteSchedule(scheduleId,username);
        }catch (AuthorizationException authorizationException){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        } catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
