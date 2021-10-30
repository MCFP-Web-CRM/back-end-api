package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.category.entity.Category;
import com.mcfuturepartners.crm.api.category.entity.QCategory;
import com.mcfuturepartners.crm.api.category.repository.CategoryRepository;
import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.entity.QCounsel;
import com.mcfuturepartners.crm.api.counsel.repository.CounselRepository;
import com.mcfuturepartners.crm.api.customer.dto.CustomerRegisterDto;
import com.mcfuturepartners.crm.api.customer.dto.CustomerResponseDto;
import com.mcfuturepartners.crm.api.customer.dto.CustomerSearch;
import com.mcfuturepartners.crm.api.customer.dto.CustomerUpdateDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.entity.QCustomer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepositoryImpl;
import com.mcfuturepartners.crm.api.exception.DatabaseErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.order.dto.OrderResponseDto;
import com.mcfuturepartners.crm.api.order.repository.OrderRepository;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import com.mcfuturepartners.crm.api.user.entity.QUser;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService{
    private final CustomerRepository customerRepository;
    private final CustomerRepositoryImpl qCustomerRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final CounselRepository counselRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final TokenProvider tokenProvider;

    @Override
    public List<CustomerResponseDto> searchCustomers(CustomerSearch customerSearch) {


        return qCustomerRepository.search(customerSearch).stream().map(customer -> {
                    CustomerResponseDto customerResponseDto = modelMapper.map(customer, CustomerResponseDto.class);
                    customerResponseDto.setManager(modelMapper.map(customer.getManager(), UserResponseDto.class));
                    return customerResponseDto;
                })
                .collect(Collectors.toList());

    }
    @Override
    public Boolean findCustomerIfManager(long customerId, String username) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(()->
                        new FindException(DatabaseErrorCode.CUSTOMER_NOT_FOUND.name()));
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->
                        new FindException(DatabaseErrorCode.USER_NOT_FOUND.name()));

        if(!customer.getManager().getUsername().equals(user.getUsername()))
            return false;

        return true;
    }
    @Override
    public CustomerResponseDto findCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow();
        CustomerResponseDto customerResponseDto = modelMapper.map(customer,CustomerResponseDto.class);
        customerResponseDto.setCategory(modelMapper.map(customer.getCategory(), CategoryDto.class));
        customerResponseDto.setCounselList(customer.getCounsels().stream().map(counsel -> modelMapper.map(counsel, CounselDto.class)).collect(Collectors.toList()));
        customerResponseDto.setOrderList(customer.getOrders().stream().map(order -> {
            OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);
            orderResponseDto.setProduct(modelMapper.map(order.getProduct(), ProductDto.class));
            return orderResponseDto;
        }).collect(Collectors.toList()));

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
        User user = userRepository.findById(customerUpdateDto.getManagerUserId()).orElseThrow(()->new FindException(DatabaseErrorCode.USER_NOT_FOUND.name()));
        Category category = categoryRepository.findById(customerUpdateDto.getCategoryId()).get();
        Customer customer = customerRepository.findById(customerUpdateDto.getId()).orElseThrow(()->new FindException(DatabaseErrorCode.CUSTOMER_NOT_FOUND.name()));

        customer.modifyUpdated(customerUpdateDto);
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
        Customer customer = customerRepository.findById(id).orElseThrow(()->new FindException(DatabaseErrorCode.CUSTOMER_NOT_FOUND.name()));
        counselRepository.deleteAll(customer.getCounsels());
        orderRepository.deleteAll(customer.getOrders());
        try {
            customerRepository.delete(customer);
            return "successfully done";
        } catch (Exception e){
            throw e;
        }
    }
}
