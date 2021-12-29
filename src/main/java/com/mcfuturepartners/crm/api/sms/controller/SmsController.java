package com.mcfuturepartners.crm.api.sms.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.customer.dto.CustomerSearch;
import com.mcfuturepartners.crm.api.exception.AuthorizationException;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.message.dto.MessageDto;
import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.message.service.MessageService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.sms.dto.*;
import com.mcfuturepartners.crm.api.sms.service.SmsService;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.util.sms.SmsRequestHandler;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/sms")
@RequiredArgsConstructor
public class SmsController {
    private final SmsRequestHandler smsRequestHandler;
    private final TokenProvider tokenProvider;
    private final SmsService smsService;
    private final MessageService messageService;

    @GetMapping("/category")
    @ApiOperation(value = "sms카테고리 조회 api", notes = "고객 카테고리와 카테고리 내에 현재 사원이 보낼 수 있는 고객의 수 전달(admin은 전체 고객 전달)")
    public ResponseEntity<List<CategoryCustomerPhone>> getCategories(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        List<CategoryCustomerPhone> categoryCustomerPhoneList;
        try{
            categoryCustomerPhoneList = smsService.getCategoriesWithNumberOfCustomers(
                    CustomerSearch.builder()
                            .authority(tokenProvider.getAuthentication(token).getAuthorities().toString())
                            .username(username)
                            .build());
        }catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(categoryCustomerPhoneList, HttpStatus.OK);
    }
    @GetMapping("/product")
    @ApiOperation(value = "sms 상품별 고객 조회 api", notes = "상품별 고객과 현재 사원이 보낼 수 있는 고객의 수 전달(admin은 전체 고객 전달)")
    public ResponseEntity<List<ProductCustomerPhone>> getProducts(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        //바꿔야함
        List<ProductCustomerPhone> productCustomerPhoneList;
        try{
            //바꿔야함
            productCustomerPhoneList = smsService.getProductsWithNumberOfCustomers(
                    CustomerSearch.builder()
                            .authority(tokenProvider.getAuthentication(token).getAuthorities().toString())
                            .username(username)
                            .build());
        }catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(productCustomerPhoneList, HttpStatus.OK);
    }
    @GetMapping("/category/{category-id}/phone")
    @ApiOperation(value = "카테고리 선택 시 핸드폰 번호 조회", notes = "고객 카테고리 내 고객의 핸드폰번호 전달")
    public ResponseEntity<PhoneListDto> getCategoryCustomerPhone(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                                 @PathVariable(name = "category-id") Long categoryId){

        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        PhoneListDto phoneListDto;
        try{
            phoneListDto = smsService.getCategoryCustomerPhone(categoryId,
                    CustomerSearch.builder().authority(tokenProvider.getAuthentication(token).getAuthorities().toString())
                            .username(username)
                            .build());
        }catch (AuthorizationException authorizationException){
            authorizationException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(phoneListDto,HttpStatus.OK);
    }

    @GetMapping("/product/{product-id}/phone")
    @ApiOperation(value = "카테고리 선택 시 핸드폰 번호 조회", notes = "고객 카테고리 내 고객의 핸드폰번호 전달")
    public ResponseEntity<PhoneListDto> getProductCustomerPhone(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                                 @PathVariable(name = "product-id") Long productId){

        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        PhoneListDto phoneListDto;
        try{
            phoneListDto = smsService.getProductCustomerPhone(productId,
                    CustomerSearch.builder().authority(tokenProvider.getAuthentication(token).getAuthorities().toString())
                            .username(username)
                            .build());
        }catch (AuthorizationException authorizationException){
            authorizationException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(phoneListDto,HttpStatus.OK);
    }
    @GetMapping("/reservedSms")
    @ApiOperation(value = "현재 예약 중인 sms 조회", notes = "사원의 예약 중 sms 조회 api, 쿼리스트링으로 size, page, sort 보내야함. sort=sendTime,desc&sort=smsId,desc 꼭 추가!")
    public ResponseEntity<Page<SmsResponseDto>> getReservedSms(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                               Pageable pageable){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        Page<SmsResponseDto> smsResponseList;

        try{
            smsResponseList = smsService.getReservedSms(username,pageable);
        }catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(smsResponseList,HttpStatus.OK);
    }

    @GetMapping
    @ApiOperation(value = "발송 이력 조회", notes = "사원의 메시지 발송 이력 조회, 쿼리스트링으로 size, page, sort 보내야함. sort=sendTime,desc&sort=smsId,desc 꼭 추가!")
    public ResponseEntity<Page<SmsResponseDto>> getSmsRecords(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                                      Pageable pageable){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        Page<SmsResponseDto> smsResponseList;

        try{
            smsResponseList = smsService.getSmsWithoutReserved(username,pageable);
        }catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(smsResponseList,HttpStatus.OK);
    }
    @PostMapping
    @ApiOperation(value = "문자 메시지 전송 요청 api", notes = "문자 메시지 전송 요청 api / 예약할 땐 년, 월, 일, 시간, 분 전달 / LMS 사용  2000byte 사용가능(프런트에서 2000byte 제한 걸어주면 좋을 듯)")
    public ResponseEntity<String> sendRequestSms(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                 @RequestBody SmsDto smsDto){

        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();
        smsDto.setUsername(username);

        ResponseEntity responseEntity = null;
        SmsProcessDto smsProcessDto = smsService.createSmsProcessDto(smsDto);

        if(ObjectUtils.isEmpty(smsDto.getReservationTime())){
            responseEntity = smsRequestHandler.sendMessage(smsProcessDto);
        }

        smsService.saveAll(smsProcessDto,responseEntity);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    @DeleteMapping("/reservedSms")
    @ApiOperation(value = "예약 취소 sms", notes = "쿼리 스트링으로 sms-id를 보내주면 해당 예약 문자 삭제 / 쿼리 스트링 없을 시 전체 예약 문자 일괄 삭제")
    public ResponseEntity<Void> deleteAllReservedSms(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                     @RequestParam(value = "sms-id",required = false) List<Long> smsId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        try{
            smsService.deleteReservedSms(smsId,username);
        }catch (AuthorizationException authorizationException){
            authorizationException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @DeleteMapping
    @ApiOperation(value = "발신 기록 삭제 api", notes = "쿼리 스트링으로 sms-id를 보내주면 해당 발송 내역 삭제 / 쿼리 스트링 없을 시 전체 발송 내역 일괄 삭제")
    public ResponseEntity<Void> deleteSelectedSmsRecords(@RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken,
                                                         @RequestParam(value = "sms-id",required = false) List<Long> smsId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        try{
            smsService.deleteSmsRecords(smsId,username);
        }catch (AuthorizationException authorizationException){
            authorizationException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
