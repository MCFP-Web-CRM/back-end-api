package com.mcfuturepartners.crm.api.customer.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.customer.dto.*;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(path = "/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final ModelMapper mapper;
    private final TokenProvider tokenProvider;

    @GetMapping
    @ApiOperation(value = "고객 조회 api", notes = "고객 조회 api, 다중 검색 조건(query string)으로 검색 가능 / sort=id,desc 쿼리스트링으로 넣어주면 가장 최신 고객부터 페이지처리")
    public ResponseEntity<Page<CustomerResponseDto>> getCustomerList(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                          @RequestParam(value = "name") @Nullable String customerName,
                                          @RequestParam(value = "phone") @Nullable String customerPhone,
                                          @RequestParam(value = "customer-category") @Nullable String customerCategory,
                                          @RequestParam(value = "product-name") @Nullable String productName,
                                          @RequestParam(value = "funnel-id") @Nullable Long funnelId,
                                          @RequestParam(value = "manager-id") @Nullable Long managerId,
                                          @RequestParam(value = "start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Nullable LocalDate startDate,
                                          @RequestParam(value = "end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Nullable LocalDate endDate,
                                          @RequestParam(value = "counsel-keyword") @Nullable String counselKeyword,
                                          Pageable pageable){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();


        Page<CustomerResponseDto> listCustomer = customerService.searchCustomers(
                CustomerSearch.builder()
                        .customerName(customerName)
                        .customerPhone(customerPhone)
                        .categoryName(customerCategory)
                        .productName(productName)
                        .funnelId(funnelId)
                        .managerId(managerId)
                        .startDate(startDate)
                        .endDate(endDate)
                        .counselKeyword(counselKeyword)
                        .authority(tokenProvider.getAuthentication(token).getAuthorities().toString())
                        .username(username)
                        .build(),pageable);

        return new ResponseEntity<>(listCustomer, HttpStatus.OK);


       /* return new ResponseEntity<>(listCustomer.stream()
                .filter(customerResponseDto -> customerResponseDto
                                                .getManager().getUsername().equals(username))
                .collect(Collectors.toList()), HttpStatus.OK);*/
    }

    @GetMapping(path = "/{customer-id}")
    @ApiOperation(value = "고객 상세 조회 api", notes = "고객 조회 api")
    public ResponseEntity<CustomerResponseDto> getCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                           @PathVariable("customer-id") long customerId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(customerService.findCustomerIfManager(customerId,username)||tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())) {

            return new ResponseEntity<>(customerService.findCustomer(customerId), HttpStatus.OK);
        }
        return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);

    }
    @PostMapping
    @ApiOperation(value = "고객 저장 api", notes = "고객 저장 api")
    public ResponseEntity<String> saveCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                               @RequestBody CustomerRegisterDto customerRegisterDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        customerRegisterDto.setManagerUsername(username);

        if(customerService.save(customerRegisterDto).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("saved",HttpStatus.OK);
    }
    @PutMapping(path="/{customer-id}")
    @ApiOperation(value = "고객 수정 api", notes = "고객 수정 api")
    public ResponseEntity<String> updateCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                 @PathVariable("customer-id") long customerId,
                                                 @RequestBody CustomerUpdateDto customerUpdateDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())||customerService.findCustomerIfManager(customerId,username)){

            customerUpdateDto.setId(customerId);

            customerService.updateCustomer(customerUpdateDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
    @DeleteMapping(path="/{customer-id}")
    @ApiOperation(value = "고객 삭제 api", notes = "고객 삭제 api")
    public ResponseEntity<String> deleteCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                 @PathVariable("customer-id") long customerId) {
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if (tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString()) || customerService.findCustomerIfManager(customerId, username)) {

            customerService.deleteCustomer(customerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
    @GetMapping(path = "/status")
    @ApiOperation(value = "당일 고객 변화 상태", notes = "고객추가, 상담, 결제추가를 바탕으로 당일 변화된 고객의 상태를 체크")
    public ResponseEntity<List<CustomerStatusCountDto>> getDailyCustomerStatus(){
        return new ResponseEntity<>(customerService.getDailyCustomerStatus(),HttpStatus.OK);
    }
    @GetMapping(path = "/funnel")
    @ApiOperation(value = "당일 고객 유입경로 통계", notes = "당일 추가된 고객들의 유입 경로에 대한 통계 제공")
    public ResponseEntity<List<CustomerFunnelCountDto>> getDailyCustomerFunnel(){
        return new ResponseEntity<>(customerService.getDailyFunnelCount(),HttpStatus.OK);
    }
}
