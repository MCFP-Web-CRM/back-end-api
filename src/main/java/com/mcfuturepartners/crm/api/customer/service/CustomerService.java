package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.customer.entity.Customer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CustomerService {
    List<Customer> findAllCustomer();
    Customer save(Customer customer);
    List<Customer> selectCustomer(Map<String,String> map);
    boolean updateCustomer(Customer customer);
    boolean deleteCustomer(Long customerno);
}