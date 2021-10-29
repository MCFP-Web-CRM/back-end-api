package com.mcfuturepartners.crm.api.customer.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.customer.dto.CustomerRegisterDto;
import com.mcfuturepartners.crm.api.customer.dto.CustomerResponseDto;
import com.mcfuturepartners.crm.api.customer.dto.CustomerUpdateDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/customer")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final ModelMapper mapper;

    @GetMapping(path="/search")
    public ResponseEntity<List<CustomerResponseDto>> searchCustomer(@RequestParam(value = "customer-category") @Nullable String customerCategory,
                                                         @RequestParam(value = "product-name") @Nullable String productName,
                                                         @RequestParam(value = "funnel") @Nullable String funnel,
                                                         @RequestParam(value = "manager") @Nullable String username,
                                                         @RequestParam(value = "start-date") @Nullable LocalDateTime startDateTime,
                                                         @RequestParam(value = "end-date") @Nullable LocalDateTime endDateTime,
                                                         @RequestParam(value = "special-note") @Nullable String specialNote){
        //logic 추가 필요
        List<CustomerResponseDto> listCustomer = customerService.findAllCustomer();
        if(listCustomer==null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }else{
            return  new ResponseEntity<>(listCustomer, HttpStatus.OK);
        }
    }
    @GetMapping
    public ResponseEntity<List<CustomerResponseDto>> getCustomerList(){
        List<CustomerResponseDto> listCustomer = customerService.findAllCustomer();
        if(listCustomer==null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }else{
            return  new ResponseEntity<>(listCustomer, HttpStatus.OK);
        }

    }
    @GetMapping(path = "/{customer-id}")
    public ResponseEntity<CustomerResponseDto> getCustomer(@PathVariable("customer-id") long customerId){
        return new ResponseEntity<>(customerService.findCustomer(customerId),HttpStatus.OK);
    }
    @PostMapping
    public ResponseEntity<String> saveCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                               @RequestBody CustomerRegisterDto customerDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        customerDto.setManagerUsername(username);

        if(customerService.save(customerDto).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("saved",HttpStatus.OK);
    }
    @PutMapping(path="{customer-id}")
    public ResponseEntity<String> updateCustomer(@PathVariable("customer-id") long customerId,
                                                 @RequestBody CustomerUpdateDto customerUpdateDto){
        customerUpdateDto.setId(customerId);
        if(customerService.updateCustomer(customerUpdateDto).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("updated",HttpStatus.OK);
    }
    @DeleteMapping(path="{customer-id}")
    public ResponseEntity<String> deleteCustomer(@PathVariable("customer-id") long customerid){
        if(customerService.deleteCustomer(customerid).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }
}
