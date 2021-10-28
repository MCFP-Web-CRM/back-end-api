package com.mcfuturepartners.crm.api.customer.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mcfuturepartners.crm.api.customer.dto.CustomerDto;
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
    public ResponseEntity<List<Customer>> searchCustomer(@RequestParam(value = "customer-category") @Nullable String customerCategory,
                                                         @RequestParam(value = "product-name") @Nullable String productName,
                                                         @RequestParam(value = "funnel") @Nullable String funnel,
                                                         @RequestParam(value = "manager") @Nullable String username,
                                                         @RequestParam(value = "start-date") @Nullable LocalDateTime startDateTime,
                                                         @RequestParam(value = "end-date") @Nullable LocalDateTime endDateTime,
                                                         @RequestParam(value = "special-note") @Nullable String specialNote){
        //logic 추가 필요
        List<Customer> listCustomer = customerService.findAllCustomer();
        if(listCustomer==null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }else{
            return  new ResponseEntity<>(listCustomer, HttpStatus.OK);
        }
    }
    @GetMapping(path="")
    public ResponseEntity<List<Customer>> getCustomerList(){
        List<Customer> listCustomer = customerService.findAllCustomer();
        if(listCustomer==null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }else{
            return  new ResponseEntity<>(listCustomer, HttpStatus.OK);
        }

    }
    @PostMapping
    public ResponseEntity<String> saveCustomer(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                               @RequestBody CustomerDto customerDto){
        String token = bearerToken.replace("Bearer ", "");
        DecodedJWT decodedJWT = JWT.decode(token);
        String username = decodedJWT.getSubject();

        customerDto.setManagerUsername(username);

        if(customerService.save(customerDto).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("saved",HttpStatus.OK);
    }
    @PutMapping(path="")
    public ResponseEntity<String> updateCustomer(@RequestBody @Valid Customer customer){
        if(customerService.updateCustomer(customer).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("updated",HttpStatus.OK);
    }
    @DeleteMapping(path="")
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerid") long customerid){
        if(customerService.deleteCustomer(customerid).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }
}
