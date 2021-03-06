package com.mcfuturepartners.crm.api.sms.service;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.category.entity.Category;
import com.mcfuturepartners.crm.api.category.repository.CategoryRepository;
import com.mcfuturepartners.crm.api.category.service.CategoryService;
import com.mcfuturepartners.crm.api.customer.dto.CustomerSearch;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepositoryImpl;
import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import com.mcfuturepartners.crm.api.exception.AuthorizationException;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.message.dto.MessageDto;
import com.mcfuturepartners.crm.api.message.entity.Message;
import com.mcfuturepartners.crm.api.message.repository.MessageRepository;
import com.mcfuturepartners.crm.api.product.dto.ProductDto;
import com.mcfuturepartners.crm.api.product.entity.Product;
import com.mcfuturepartners.crm.api.product.repository.ProductRepository;
import com.mcfuturepartners.crm.api.sms.dto.*;
import com.mcfuturepartners.crm.api.sms.entity.Sms;
import com.mcfuturepartners.crm.api.sms.entity.SmsStatus;
import com.mcfuturepartners.crm.api.sms.repository.SmsRepository;
import com.mcfuturepartners.crm.api.sms.repository.SmsRepositoryImpl;
import com.mcfuturepartners.crm.api.user.entity.Authority;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import com.mcfuturepartners.crm.api.util.sms.SmsRequestHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SmsServiceImpl implements SmsService{
    private final SmsRepository smsRepository;
    private final CustomerRepository customerRepository;
    private final CustomerRepositoryImpl qCustomerRepository;
    private final CustomerService customerService;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final SmsRepositoryImpl qSmsRepository;
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<CategoryCustomerPhone> getCategoriesWithNumberOfCustomers(CustomerSearch customerSearch) {
        User user = userRepository.findByUsername(customerSearch.getUsername()).orElseThrow(()->new FindException("USER "+ ErrorCode.RESOURCE_NOT_FOUND));

        List<Category> categories = categoryRepository.findAll();
        List<CategoryCustomerPhone> categoryCustomerPhones = new ArrayList<>();

        if(!customerSearch.getAuthority().contains(Authority.ADMIN.toString())){
            customerSearch.setUserId(user.getId());
        }

        for(Category category: categories){
            customerSearch.setCategoryName(category.getName());
            categoryCustomerPhones.add(
                    CategoryCustomerPhone.builder()
                            .categoryDto(modelMapper.map(category, CategoryDto.class))
                            .customerCount(qCustomerRepository
                                    .searchWithoutPageable(customerSearch).stream()
                                    .count())
                            .build());
        }
        return categoryCustomerPhones;
    }

    @Override
    public List<ProductCustomerPhone> getProductsWithNumberOfCustomers(CustomerSearch customerSearch) {
        User user = userRepository.findByUsername(customerSearch.getUsername()).orElseThrow(()->new FindException("USER "+ ErrorCode.RESOURCE_NOT_FOUND));

        List<Product> products = productRepository.findAll();
        List<ProductCustomerPhone> productCustomerPhones = new ArrayList<>();

        if(!customerSearch.getAuthority().contains(Authority.ADMIN.toString())){
            customerSearch.setUserId(user.getId());
        }

        for(Product product: products){
            customerSearch.setProductName(product.getName());
            productCustomerPhones.add(
                    ProductCustomerPhone.builder()
                            .productDto(modelMapper.map(product, ProductDto.class))
                            .customerCount(qCustomerRepository
                                    .searchWithoutPageable(customerSearch).stream()
                                    .count())
                            .build());
        }
        return productCustomerPhones;
    }

    @Override
    public PhoneListDto getCategoryCustomerPhone(Long categoryId, CustomerSearch customerSearch) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(()->new FindException("Category "+ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        User user = userRepository.findByUsername(customerSearch.getUsername()).orElseThrow(()->new AuthorizationException("USER "+ ErrorCode.RESOURCE_NOT_FOUND));

        if(!customerSearch.getAuthority().contains(Authority.ADMIN.toString())){
            customerSearch.setUserId(user.getId());
        }
        customerSearch.setCategoryName(category.getName());
        List<Customer> customerList = qCustomerRepository.searchWithoutPageable(customerSearch);

        return PhoneListDto.builder()
                .category(modelMapper.map(category, CategoryDto.class))
                .receiverPhone(customerList.stream().map(customer -> customer.getPhone()).collect(Collectors.toList()))
                .build();
    }

    @Override
    public PhoneListDto getProductCustomerPhone(Long productId, CustomerSearch customerSearch) {
        Product product = productRepository.findById(productId).orElseThrow(()-> new FindException("Product "+ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        User user = userRepository.findByUsername(customerSearch.getUsername()).orElseThrow(()->new AuthorizationException("USER "+ ErrorCode.RESOURCE_NOT_FOUND));

        if(!customerSearch.getAuthority().contains(Authority.ADMIN.toString())){
            customerSearch.setUserId(user.getId());
        }
        customerSearch.setProductName(product.getName());
        List<Customer> customerList = qCustomerRepository.searchWithoutPageable(customerSearch);
        return PhoneListDto.builder()
                .product(modelMapper.map(product, ProductDto.class))
                .receiverPhone(customerList.stream().map(customer -> customer.getPhone()).collect(Collectors.toList()))
                .build();
    }

    @Override
    public Page<SmsResponseDto> getReservedSms(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new AuthorizationException("USER "+ ErrorCode.RESOURCE_NOT_FOUND));
        Page<Sms> searchResult =smsRepository.findAllBySenderAndSmsStatus(user,SmsStatus.RESERVED, pageable);

        return new PageImpl<>(searchResult.stream().map(sms -> {
            SmsResponseDto smsResponseDto = modelMapper.map(sms,SmsResponseDto.class);
            smsResponseDto.setSenderName(sms.getSender().getName());
            return smsResponseDto;
        }).collect(Collectors.toList()),pageable,searchResult.getTotalElements());
    }

    @Override
    public Page<SmsResponseDto> getSmsWithoutReserved(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new AuthorizationException("USER "+ ErrorCode.RESOURCE_NOT_FOUND));
        Page<Sms> searchResult = smsRepository.findAllBySenderAndSmsStatusIsNot(user,SmsStatus.RESERVED, pageable);


        return new PageImpl<>(searchResult.stream().map(sms -> {
            SmsResponseDto smsResponseDto = modelMapper.map(sms,SmsResponseDto.class);
            smsResponseDto.setSenderName(sms.getSender().getName());
            return smsResponseDto;
        }).collect(Collectors.toList()),pageable, searchResult.getTotalElements());
    }


    @Override
    public List<Sms> saveAll(SmsProcessDto smsDto, ResponseEntity responseEntity) {
        User user = userRepository.findByUsername(smsDto.getUsername()).orElseThrow(()-> new FindException("User "+ ErrorCode.RESOURCE_NOT_FOUND));

        List<Sms> smsList = smsDto.getReceiverPhone().stream().map(
                phone -> Sms.builder()
                    .message(smsDto.getMessage())
                    .receiver(customerRepository.findFirstByPhone(phone).orElseThrow(()->new FindException("Customer "+ ErrorCode.RESOURCE_NOT_FOUND)))
                    .sender(user)
                    .sendTime(smsDto.getReservationTime())
                    .build()).collect(Collectors.toList());

        if(responseEntity.getStatusCode().equals(HttpStatus.OK)){
            if(ObjectUtils.isEmpty(smsDto.getReservationTime())){
                return smsRepository.saveAll(smsList.stream().peek(sms -> sms.setSmsStatus(SmsStatus.SENT))
                        .peek(sms -> sms.setSendTime(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                        .collect(Collectors.toList()));
            }
            return smsRepository.saveAll(smsList.stream().peek(sms -> sms.setSmsStatus(SmsStatus.RESERVED))
                    .collect(Collectors.toList()));

        }
        return smsRepository.saveAll(smsList.stream().peek(sms -> sms.setSmsStatus(SmsStatus.FAILED))
                .peek(sms -> sms.setSendTime(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()))
                .collect(Collectors.toList()));
    }

    @Override
    public SmsProcessDto createSmsProcessDto(SmsDto smsDto){
        User user = userRepository.findByUsername(smsDto.getUsername()).orElseThrow(()-> new FindException("User "+ ErrorCode.RESOURCE_NOT_FOUND));

        Message message = messageRepository.save(Message.builder()
                .title(smsDto.getTitle())
                .user(user)
                .content(smsDto.getContent())
                .build());

        return SmsProcessDto.builder()
                .message(message)
                .username(user.getUsername())
                .reservationTime(smsDto.getReservationTime())
                .receiverPhone(smsDto.getReceiverPhone()).build();
    }


    @Override
    public List<Sms> updateReservedSmsTo(SmsProcessDto smsProcessDto) {
        List<Sms> smsList = smsRepository.findAllById(smsProcessDto.getSmsIds());

        smsList = smsRepository.saveAll(smsList.stream().map(sms -> {
                sms.setSmsStatus(SmsStatus.SENT);
                sms.setSendTime(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime());
            return sms;
        }).collect(Collectors.toList()));
        return smsList;
    }

    @Override
    public void deleteReservedSms(List<Long> smsIds, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new AuthorizationException("USER "+ErrorCode.UNAUTHORIZED));
        List<Sms> smsList ;

        if(ObjectUtils.isEmpty(smsIds)){
            smsList = smsRepository.findAllBySenderAndSmsStatus(user, SmsStatus.RESERVED);
        }
        else{
            smsList = smsRepository.findAllById(smsIds).stream().filter(sms -> sms.getSender().equals(user)).filter(sms -> sms.getSmsStatus().equals(SmsStatus.RESERVED)).collect(Collectors.toList());
        }

        if(smsList.size()==0){
            throw new FindException(ErrorCode.BAD_REQUEST.getMsg());
        }

        try{
            smsRepository.deleteAll(smsList);
        }catch (Exception e){
            throw e;
        }

    }


    @Override
    public void deleteSmsRecords(List<Long> smsIds, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new AuthorizationException("USER "+ErrorCode.UNAUTHORIZED));
        List<Sms> smsList ;

        if(ObjectUtils.isEmpty(smsIds)){
            smsList = smsRepository.findAllBySenderAndSmsStatusIsNot(user, SmsStatus.RESERVED);
        }
        else {
            smsList = smsRepository.findAllById(smsIds).stream().filter(sms -> sms.getSender().equals(user)).filter(sms -> !sms.getSmsStatus().equals(SmsStatus.RESERVED)).collect(Collectors.toList());
        }

        if(smsList.size()==0){
            throw new FindException(ErrorCode.BAD_REQUEST.getMsg());
        }

        try{
            smsRepository.deleteAll(smsList);
        }catch (Exception e){
            throw e;
        }
    }

    @Override
    public List<Sms> updateReservedSmsTo(LocalDateTime localDateTime) {
        List<Sms> smsList = qSmsRepository.findReservedSmsBeforeNow(localDateTime);
        try{
            smsRepository.saveAll(smsList.stream().map(sms -> {
                sms.setSmsStatus(SmsStatus.SENT);
                return sms;
            }).collect(Collectors.toList()));

        } catch(Exception e){
            throw e;
        }
        return smsList;
    }

}
