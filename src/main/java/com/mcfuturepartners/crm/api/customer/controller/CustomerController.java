package com.mcfuturepartners.crm.api.customer.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.customer.dto.CustomerRegisterDto;
import com.mcfuturepartners.crm.api.customer.dto.CustomerResponseDto;
import com.mcfuturepartners.crm.api.customer.dto.CustomerSearch;
import com.mcfuturepartners.crm.api.customer.dto.CustomerUpdateDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    @ApiOperation(value = "고객 조회 api", notes = "고객 조회 api, 다중 검색 조건(query string)으로 검색 가능")
    public ResponseEntity<List<CustomerResponseDto>> getCustomerList(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                                     @RequestParam(value = "customer-category") @Nullable String customerCategory,
                                                                     @RequestParam(value = "product-name") @Nullable String productName,
                                                                     @RequestParam(value = "funnel") @Nullable String funnel,
                                                                     @RequestParam(value = "manager-id") @Nullable Long managerId,
                                                                     @RequestParam(value = "start-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Nullable LocalDate startDate,
                                                                     @RequestParam(value = "end-date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Nullable LocalDate endDate,
                                                                     @RequestParam(value = "counsel-keyword") @Nullable String counselKeyword){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        List<CustomerResponseDto> listCustomer = customerService.searchCustomers(
                CustomerSearch.builder()
                        .categoryName(customerCategory)
                        .productName(productName)
                        .funnel(funnel)
                        .managerId(managerId)
                        .startDate(startDate)
                        .endDate(endDate)
                        .counselKeyword(counselKeyword)
                        .build());

        if(tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){
            return new ResponseEntity<>(listCustomer, HttpStatus.OK);
        }

        return new ResponseEntity<>(listCustomer.stream()
                .filter(customerResponseDto -> customerResponseDto
                                                .getManager().getUsername().equals(username))
                .collect(Collectors.toList()), HttpStatus.OK);
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
    @PutMapping(path="{customer-id}")
    @ApiOperation(value = "고객 수정 api", notes = "고객 수정 api")
    public ResponseEntity<String> updateCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                 @PathVariable("customer-id") long customerId,
                                                 @RequestBody CustomerUpdateDto customerUpdateDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(customerService.findCustomerIfManager(customerId,username)||tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){

            customerUpdateDto.setId(customerId);

            customerService.updateCustomer(customerUpdateDto);
            return new ResponseEntity<>(HttpStatus.OK);
        }

        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

    }
    @DeleteMapping(path="{customer-id}")
    @ApiOperation(value = "고객 삭제 api", notes = "고객 삭제 api")
    public ResponseEntity<String> deleteCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                 @PathVariable("customer-id") long customerId){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        if(customerService.findCustomerIfManager(customerId,username)||tokenProvider.getAuthentication(token).getAuthorities().toString().contains(Authority.ADMIN.toString())){


            customerService.deleteCustomer(customerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>( HttpStatus.UNAUTHORIZED);
    }
}
