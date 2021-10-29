package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.category.entity.Category;
import com.mcfuturepartners.crm.api.category.repository.CategoryRepository;
import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.customer.dto.CustomerRegisterDto;
import com.mcfuturepartners.crm.api.customer.dto.CustomerResponseDto;
import com.mcfuturepartners.crm.api.customer.dto.CustomerUpdateDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<CustomerResponseDto> findAllCustomer() {
        List<Customer> customerList = customerRepository.findAll();

        return customerList.stream().map(customer -> modelMapper.map(customer, CustomerResponseDto.class)).collect(Collectors.toList());
    }

    @Override
    public CustomerResponseDto findCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        CustomerResponseDto customerResponseDto = modelMapper.map(customer,CustomerResponseDto.class);
        customerResponseDto.setCounselList(customer.getCounsels().stream().map(counsel -> modelMapper.map(counsel, CounselDto.class)).collect(Collectors.toList()));
        customerResponseDto.setOrderList(customer.getOrders().stream().map(order -> modelMapper.map(order, OrderDto.class)).collect(Collectors.toList()));

        return customerResponseDto;
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
    public String save(CustomerRegisterDto customerDto) {
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
    public String updateCustomer(CustomerUpdateDto customerUpdateDto) {
        User user = userRepository.findById(customerUpdateDto.getManagerUserId()).get();
        Category category = categoryRepository.findById(customerUpdateDto.getCategoryId()).get();
        Customer customer = customerUpdateDto.toEntity();
        customer.setCategory(category);
        customer.setManager(user);
        try {
            customerRepository.save(customer);
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
