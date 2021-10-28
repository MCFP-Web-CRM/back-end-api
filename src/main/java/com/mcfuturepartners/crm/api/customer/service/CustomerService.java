package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.customer.dto.CustomerDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findAllCustomer();
    Customer findCustomer(Long id);
    String save(CustomerDto customerDto);
    List<Customer> selectCustomer(Map<String,String> map);
    String updateCustomer(Customer customer);
    String deleteCustomer(Long id);
}