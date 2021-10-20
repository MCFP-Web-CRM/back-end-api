package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;

    @Override
    public List<Customer> findAllCustomer() {
        List<Customer> customerList = customerRepository.findAll();
        return customerList;
    }

    @Override
    public Customer findCustomer(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(()-> new HttpClientErrorException(HttpStatus.NOT_FOUND,"doesn't exist customer"));
    }

    @Override
    public List<Customer> selectCustomer(Map<String,String> map) {
        List<Customer> customerList = customerRepository.findAll();
        Map<String,String> conditions = new HashMap<>();
        if(map.containsKey("businessStatus")){
            conditions.put("businessStatus",map.get("businessStatus"));
        }
        return customerList;
    }

    @Override
    public String save(Customer customer) {
        try {
            Optional<Customer> exist = customerRepository.findByNameAndPhone(customer.getName(),customer.getPhone());
            if(exist.isPresent()){
                return "customer already exist";
            }else{
                customerRepository.save(customer);
                return "successfully done";
            }
        } catch(Exception e){
            throw e;
        }
    }

    @Override
    public String updateCustomer(Customer customer) {
        try {
            customerRepository.save(customerRepository.findById(customer.getId()).get());
            return "successfully done";
        } catch (Exception e){
            throw e;
        }
    }
    @Override
    public String deleteCustomer(Long id) {
        try {
            customerRepository.delete(customerRepository.findById(id).get());
            return "successfully done";
        } catch (Exception e){
            throw e;
        }
    }
}
