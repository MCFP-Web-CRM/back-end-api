package com.mcfuturepartners.crm.api.customer.service;

import com.mcfuturepartners.crm.api.admin.customer.ManagerChangeDto;
import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.category.entity.Category;
import com.mcfuturepartners.crm.api.category.entity.QCategory;
import com.mcfuturepartners.crm.api.category.repository.CategoryRepository;
import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.counsel.entity.CounselStatus;
import com.mcfuturepartners.crm.api.counsel.entity.QCounsel;
import com.mcfuturepartners.crm.api.counsel.repository.CounselRepository;
import com.mcfuturepartners.crm.api.customer.dto.*;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.entity.CustomerStatus;
import com.mcfuturepartners.crm.api.customer.entity.QCustomer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepositoryImpl;
import com.mcfuturepartners.crm.api.exception.DatabaseErrorCode;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.funnel.dto.FunnelResponseDto;
import com.mcfuturepartners.crm.api.funnel.entity.Funnel;
import com.mcfuturepartners.crm.api.funnel.repository.FunnelRepository;
import com.mcfuturepartners.crm.api.order.dto.OrderDto;
import com.mcfuturepartners.crm.api.order.dto.OrderResponseDto;
import com.mcfuturepartners.crm.api.order.repository.OrderRepository;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.refund.dto.RefundResponseDto;
import com.mcfuturepartners.crm.api.security.jwt.TokenProvider;
import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import com.mcfuturepartners.crm.api.user.entity.QUser;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;

import javax.persistence.OrderBy;
import javax.validation.constraints.Null;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerRepositoryImpl qCustomerRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;
    private final FunnelRepository funnelRepository;

    @Override
    public Page<CustomerResponseDto> searchCustomers(CustomerSearch customerSearch, Pageable pageable) {
        User user = userRepository.findByUsername(customerSearch.getUsername()).get();

        if(!customerSearch.getAuthority().contains(Authority.ADMIN.toString())){
            customerSearch.setManagerId(user.getId());
        }
        Page<Customer> searchResult = qCustomerRepository.search(customerSearch,pageable);

        return new PageImpl<>(searchResult.stream().map(customer -> {
                    CustomerResponseDto customerResponseDto = modelMapper.map(customer, CustomerResponseDto.class);
                    if (customer.getManager() != null)
                        customerResponseDto.setManager(modelMapper.map(customer.getManager(), UserResponseDto.class));
                    if (customer.getFunnel() != null)
                        customerResponseDto.setFunnel(modelMapper.map(customer.getFunnel(), FunnelResponseDto.class));
                    return customerResponseDto;
                })
                .collect(Collectors.toList()),pageable,searchResult.getTotalElements());

    }
    @Override
    public Boolean findCustomerIfManager(long customerId, String username) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() ->
                        new FindException(DatabaseErrorCode.CUSTOMER_NOT_FOUND.name()));
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new FindException(DatabaseErrorCode.USER_NOT_FOUND.name()));
        if(ObjectUtils.isEmpty(customer.getManager())){
            return false;
        }
        if (customer.getManager().getUsername().equals(user.getUsername()))
            return true;

        return false;
    }

    @Override
    public String saveAll(List<Customer> customerList) {
        customerRepository.saveAll(customerList);
        return "success";
    }

    @Override
    public CustomerResponseDto findCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new FindException(DatabaseErrorCode.CUSTOMER_NOT_FOUND.name()));
        CustomerResponseDto customerResponseDto = modelMapper.map(customer, CustomerResponseDto.class);

        if(customer.getCategory()!=null)
            customerResponseDto.setCategory(modelMapper.map(customer.getCategory(), CategoryDto.class));
        if(customer.getFunnel()!=null)
            customerResponseDto.setFunnel(modelMapper.map(customer.getFunnel(), FunnelResponseDto.class));
        if(customer.getCounsels()!=null)
            customerResponseDto.setCounselList(customer.getCounsels().stream().map(counsel ->{
                CounselDto counselDto = modelMapper.map(counsel,CounselDto.class);
                if(!ObjectUtils.isEmpty(counsel.getUser())){
                    counselDto.setUsername(counsel.getUser().getUsername());
                    counselDto.setName(counsel.getUser().getName());
                }
                return counselDto;
            }).sorted((o1, o2) -> o2.getRegDate().compareTo(o1.getRegDate())).collect(Collectors.toList()));
        if(customer.getOrders()!=null)
            customerResponseDto.setOrderList(customer.getOrders().stream().filter(order -> !ObjectUtils.isEmpty(order.getProduct())).map(order -> {
                OrderResponseDto orderResponseDto = modelMapper.map(order, OrderResponseDto.class);
                if(!ObjectUtils.isEmpty(order.getProduct())){
                    orderResponseDto.setProduct(modelMapper.map(order.getProduct(), ProductDto.class));
                }
                if(!ObjectUtils.isEmpty(order.getRefund())){
                    orderResponseDto.setRefundDto(modelMapper.map(order.getRefund(), RefundResponseDto.class));
                }
                return orderResponseDto;
            }).sorted((o1, o2) -> o2.getRegDate().compareTo(o1.getRegDate())).collect(Collectors.toList()));

        return customerResponseDto;
    }

    @Override
    public List<Customer> selectCustomer(Map<String, String> map) {
        List<Customer> customerList = customerRepository.findAll();
        Map<String, String> conditions = new HashMap<>();
        if (map.containsKey("businessStatus")) {
            conditions.put("businessStatus", map.get("businessStatus"));
        }
        return customerList;
    }

    @Override
    public String save(CustomerRegisterDto customerDto) {
        if (!customerRepository.findTopByPhone(customerDto.getPhone()).isEmpty()) {
            return "customer already exists";
        }

        Customer customer = customerDto.toEntity();
        if (customerDto.getFunnelId() != null)
            customer.setFunnel(funnelRepository.findById(customerDto.getFunnelId()).orElse(null));
        if (customerDto.getManagerUsername() != null)
            customer.setManager(userRepository.findByUsername(customerDto.getManagerUsername()).orElse(null));
        if (customerDto.getCategoryId() != null)
            customer.setCategory(categoryRepository.findById(customerDto.getCategoryId()).orElse(null));
        customerRepository.save(customer);
        return "successfully done";

    }

    @Override
    public String changeAllCustomersManager(ManagerChangeDto managerChangeDto) {
        User oldManager = userRepository.findById(managerChangeDto.getOldManagerId()).orElseThrow(()-> new FindException("Old Manager"+ErrorCode.RESOURCE_NOT_FOUND));
        User newManager = userRepository.findById(managerChangeDto.getNewManagerId()).orElseThrow(()-> new FindException("New Manager"+ErrorCode.RESOURCE_NOT_FOUND));

        try{
            customerRepository.saveAll(oldManager.getCustomers().stream().map(customer -> customer.changeManager(newManager)).collect(Collectors.toList()));
        } catch (Exception e){
            throw e;
        }
        return "Success";
    }

    @Override
    public String updateCustomer(CustomerUpdateDto customerUpdateDto) {

        Customer customer = customerRepository.findById(customerUpdateDto.getId()).orElseThrow(() -> new FindException(DatabaseErrorCode.CUSTOMER_NOT_FOUND.name()));
        customer.updateModified(customerUpdateDto);

        if(customerUpdateDto.getCategoryId() != null){
            Category category = categoryRepository.findById(customerUpdateDto.getCategoryId()).orElseThrow(()->new FindException("Category "+ ErrorCode.RESOURCE_NOT_FOUND));
            customer.setCategory(category);
        }

        if(customerUpdateDto.getFunnelId() != null){
            Funnel funnel = funnelRepository.findById(customerUpdateDto.getFunnelId()).orElseThrow(()->new FindException("Funnel " + ErrorCode.RESOURCE_NOT_FOUND));
            customer.setFunnel(funnel);
        }

        if(customerUpdateDto.getManagerUserId() != null){
            User user = userRepository.findById(customerUpdateDto.getManagerUserId()).orElseThrow(() -> new FindException(DatabaseErrorCode.USER_NOT_FOUND.name()));
            customer.setManager(user);
        }

        try {
            customerRepository.save(customer);
            return "successfully done";
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public String deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new FindException(DatabaseErrorCode.CUSTOMER_NOT_FOUND.name()));
        customer.removeOrdersFromCustomer();
        try {
            customerRepository.delete(customer);
            return "successfully done";
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public List<CustomerStatusCountDto> getDailyCustomerStatus() {
        List<CustomerStatusCountDto> dailyCustomerStatus = new ArrayList<>();
        //zoned 추가
        LocalDateTime todayDate = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonth(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate().getDayOfMonth()).atStartOfDay();

        dailyCustomerStatus.add(CustomerStatusCountDto.builder().customerStatus(CustomerStatus.NEWLY_ESTABILSHED)
                .count((int) customerRepository.countCustomerByRegDateIsAfter(todayDate)).build());

        dailyCustomerStatus.add(CustomerStatusCountDto.builder().customerStatus(CustomerStatus.SUCCESSFUL)
                .count((int)qCustomerRepository.countCustomersWithOrderToday(todayDate)).build());

        List<Customer> customersWithCounselToday = qCustomerRepository.findCustomersWithCounselToday(todayDate);
        CustomerStatusCountDto failedCustomer =  CustomerStatusCountDto.builder().customerStatus(CustomerStatus.FAILED).count(0).build();
        CustomerStatusCountDto onHoldCustomer =  CustomerStatusCountDto.builder().customerStatus(CustomerStatus.ON_HOLD).count(0).build();
        CustomerStatusCountDto inProgressCustomer =  CustomerStatusCountDto.builder().customerStatus(CustomerStatus.IN_PROGRESS).count(0).build();

        for(Customer customer : customersWithCounselToday){
            if(customer.getCounsels().get(customer.getCounsels().size()-1).getStatus().equals(CounselStatus.REFUSAL)){
                failedCustomer.setCount(failedCustomer.getCount()+1);
            }
            else if(customer.getCounsels().get(customer.getCounsels().size()-1).getStatus().equals(CounselStatus.ABSENCE)||customer.getCounsels().get(customer.getCounsels().size()-1).getStatus().equals(CounselStatus.LONG_TERM_ABSENCE)){
                onHoldCustomer.setCount(onHoldCustomer.getCount()+1);
            }
            else {
                inProgressCustomer.setCount(inProgressCustomer.getCount()+1);
            }
        }
        dailyCustomerStatus.add(failedCustomer);
        dailyCustomerStatus.add(onHoldCustomer);
        dailyCustomerStatus.add(inProgressCustomer);

        return dailyCustomerStatus;
    }

    @Override
    public List<CustomerFunnelCountDto> getFunnelCount() {
        List<Funnel> funnelList = funnelRepository.findAll();

        return funnelList.stream().map(funnel ->
                CustomerFunnelCountDto.builder().funnel(modelMapper.map(funnel,FunnelResponseDto.class))
                        .count(qCustomerRepository.countCustomersByFunnel(funnel)).build()).collect(Collectors.toList());
    }


}
