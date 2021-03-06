package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.admin.customer.CheckManagerChangeDto;
import com.mcfuturepartners.crm.api.admin.customer.ManagerChangeDto;
import com.mcfuturepartners.crm.api.customer.dto.*;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.entity.CustomerStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface CustomerService {
    Page<CustomerResponseDto> searchCustomers(CustomerSearch customerSearch, Pageable pageable);
    CustomerResponseDto findCustomer(Long id);
    Boolean findCustomerIfManager(long counselId, String username);
    Boolean checkCustomerExists(String phone);
    String saveAll(List<Customer> customerList);
    String save(CustomerRegisterDto customerDto);
    String save(Customer customer);
    String changeAllCustomersManager(ManagerChangeDto managerChangeDto);
    String changeMangagerOfCheckedCustomers(CheckManagerChangeDto checkManagerChangeDto);
    List<Customer> selectCustomer(Map<String,String> map);
    String updateCustomer(CustomerUpdateDto customerUpdateDto);
    String deleteCustomer(Long id);
    List<CustomerStatusCountDto> getDailyCustomerStatus();
    List<CustomerFunnelCountDto> getFunnelCount();
    String deleteMultipleCustomers(List<Long> customerIds);
}