package com.mcfuturepartners.crm.api.customer.controller;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/api")
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerService customerService;
    private final ModelMapper mapper;

    @GetMapping(path="/customer")
    public ResponseEntity<List<Customer>> searchCustomer(@Nullable Long id, @Nullable String name,@Nullable String phone){
        List<Customer> listCustomer = customerService.findAllCustomer();
        if(listCustomer==null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }else{
            return  new ResponseEntity<>(listCustomer, HttpStatus.OK);
        }
    }
    @GetMapping(path="/customerlist")
    public ResponseEntity<List<Customer>> getCustomerList(){
        List<Customer> listCustomer = customerService.findAllCustomer();
        if(listCustomer==null){
            return new ResponseEntity<>(null,HttpStatus.NOT_FOUND);
        }else{
            return  new ResponseEntity<>(listCustomer, HttpStatus.OK);
        }

    }
    @PostMapping(path="/customer")
    public ResponseEntity<String> saveCustomer(@RequestBody @Valid Customer customer){
        if(customerService.save(mapper.map(customer, Customer.class)).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }else{

        }
        return new ResponseEntity<>("saved",HttpStatus.OK);
    }
    @PutMapping(path="/customer")
    public ResponseEntity<String> updateCustomer(@RequestBody @Valid Customer customer){
        if(customerService.updateCustomer(customer).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("updated",HttpStatus.OK);
    }
    @DeleteMapping(path="/customer")
    public ResponseEntity<String> deleteCustomer(@PathVariable("customerid") long customerid){
        if(customerService.deleteCustomer(customerid).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }
}
