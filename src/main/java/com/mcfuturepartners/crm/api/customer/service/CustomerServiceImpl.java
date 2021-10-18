package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    public Customer save(Customer customer) {
        Customer save = customerRepository.save(customer);
        System.out.println("save.getRegDate() = " + save.getRegDate());
        return save;
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
    public boolean updateCustomer(Customer customer) {
        if(customerRepository.existsById(customer.getNo())){
            //update처리
//            customerRepository.deleteById(customerno);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public boolean deleteCustomer(Long customerno) {
        if(customerRepository.existsById(customerno)){
            customerRepository.deleteById(customerno);
            return true;
        }else{
            return false;
        }
    }

//    @Override
//    public List<Customer> findAllCustomer() {
//        List<Customer> allCustomer = customerRepository.findAllCustomer();
//        //받아온 객체 json형식 string으로 반환
//        if(allCustomer.isEmpty()){
//            return null;
//        }else {
//            return allCustomer;
//        }
//    }
//    @Override
//    public List<Customer> selectCustomer(String keyword, String type) {
//        List<Customer> allCustomer = customerRepository.findAllCustomer();
//        //조건에 맞는 고객데이터 json형식 string으로 반환
//        if(allCustomer.isEmpty()){
//            return null;
//        }else {
//            List<Customer> result = null;
//            for (Customer customer : allCustomer) {
//
//            }
//            return result;
//        }
//    }
//    @Override
//    public boolean save(Customer customer) {
//        if(customerRepository.registerCustomer(customer)){
//            return true;
//        }else{
//            return false;
//        }
//    }
//    @Override
//    public boolean updateCustomer(Customer customer) {
//        if(customerRepository.updateCustomer(customer)){
//            return true;
//        }else{
//            return false;
//        }
//    }
//    @Override
//    public boolean deleteCustomer(String customerno) {
//        if(customerRepository.deleteCustomer(customerno)){
//            return true;
//        }else{
//            return false;
//        }
//    }
}
