package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.customer.dto.CustomerRegisterDto;
import com.mcfuturepartners.crm.api.customer.dto.CustomerResponseDto;
import com.mcfuturepartners.crm.api.customer.dto.CustomerUpdateDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    List<CustomerResponseDto> findAllCustomer();
    CustomerResponseDto findCustomer(Long id);
    String save(CustomerRegisterDto customerDto);
    List<Customer> selectCustomer(Map<String,String> map);
    String updateCustomer(CustomerUpdateDto customerUpdateDto);
    String deleteCustomer(Long id);
}