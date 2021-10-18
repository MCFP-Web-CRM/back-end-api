package com.mcfuturepartners.crm.api.customer.controller;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import com.mcfuturepartners.crm.api.security.filter.TokenFilter;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.entity.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final ModelMapper mapper;

    @PostMapping(path="/searchCustomer")
    public ResponseEntity<String> searchCustomer(@RequestBody Map<String,String> map){

        String keyword = map.get("keyword");
        String type = map.get("type");
        //type에 따라 실행되는 Query메소드 분기, 현재는 모든 고객을 받아와 맞는 조건에 맞는 고객만 반환(json String)
        List<Customer> result = customerService.selectCustomer(map);
        return ResponseEntity.ok("Log in failed");
    }

    @PostMapping(path="/listCustomer")
    public ResponseEntity<String> getCustomerList(){
        List<Customer> listCustomer = customerService.findAllCustomer();
        if(listCustomer==null){

        }else{

        }
        return ResponseEntity.ok("Log in failed");
    }
    @PostMapping(path="/saveCustomer")
    public ResponseEntity<String> saveCustomer(@RequestBody @Valid Customer customer){
        Customer save = customerService.save(mapper.map(customer, Customer.class));


        return ResponseEntity.ok("Log in failed");
    }
    @PostMapping(path="/updateCustomer")
    public ResponseEntity<String> updateCustomer(@RequestBody @Valid Customer customer){
        if(customerService.updateCustomer(customer)){

        }else {

        }
        return ResponseEntity.ok("Log in failed");
    }
    @PostMapping(path="/deleteCustomer")
    public ResponseEntity<String> deleteCustomer(@RequestBody Map<String,String> map){
        Long customerno = Long.parseLong(map.get("customerno"));
        if(customerService.deleteCustomer(customerno)){

        }else {

        }
        return ResponseEntity.ok("Log in failed");
    }
}
