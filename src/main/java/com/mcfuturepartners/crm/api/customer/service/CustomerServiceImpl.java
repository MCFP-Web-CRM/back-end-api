package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.category.repository.CategoryRepository;
import com.mcfuturepartners.crm.api.customer.dto.CustomerDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
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
    public String save(CustomerDto customerDto) {
        try {
            Optional<Customer> exist = customerRepository.findByNameAndPhone(customerDto.getName(),customerDto.getPhone());
            if(exist.isPresent()){
                return "customer already exist";
            }else{
                Customer customer = customerDto.toEntity();
                customer.setManager(userRepository.findByUsername(customerDto.getManagerUsername()).orElseThrow());
                customer.setCategory(categoryRepository.findById(customerDto.getCategoryId()).orElseThrow());
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
