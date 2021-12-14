package com.mcfuturepartners.crm.api.counsel.service;

import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.dto.CounselResponseDto;
import com.mcfuturepartners.crm.api.counsel.dto.CounselUpdateDto;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.counsel.repository.CounselRepository;
import com.mcfuturepartners.crm.api.exception.DatabaseErrorCode;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class CounselServiceImpl implements CounselService {
    private final CounselRepository counselRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    @Override
    public CounselResponseDto wrapCounselResponseDto(Counsel counsel) {
        CounselResponseDto counselResponseDto = modelMapper.map(counsel, CounselResponseDto.class);
        if(!ObjectUtils.isEmpty(counsel.getUser())){
            counselResponseDto.setUser(modelMapper.map(counsel.getUser(), UserResponseDto.class));
        }
        return counselResponseDto;
    }

    @Override
    public List<CounselResponseDto> saveCounsel(CounselDto counselDto) {
        Counsel counsel = counselDto.toEntity();
        Customer customer = customerRepository.getById(counselDto.getCustomerId());
        customer.setCounselStatus(counsel.getStatus());
        counsel.setCustomer(customer);
        counsel.setUser(userRepository.getByUsername(counselDto.getUsername()));

        customerRepository.save(customer);
        counselRepository.save(counsel);

        return counselRepository.findAllByCustomer(customer).stream()
                .map(counsel1 -> wrapCounselResponseDto(counsel1)).collect(Collectors.toList());
    }
    @Override
    public List<CounselResponseDto> findAll() {
        return counselRepository.findAll().stream()
                .map(counsel -> wrapCounselResponseDto(counsel)).collect(Collectors.toList());
    }

    @Override
    public List<CounselResponseDto> findAllByUsername(String username) {
        return counselRepository.findAllByUser(userRepository.findByUsername(username).orElseThrow(()-> new FindException(DatabaseErrorCode.USER_NOT_FOUND.name()))).stream()
                .map(counsel -> wrapCounselResponseDto(counsel)).collect(Collectors.toList());
    }

    @Override
    public List<Counsel> findAllByUserId(long userId) {
        return counselRepository.findAllByUser(userRepository.findById(userId).get());
    }

    @Override
    public Optional<Counsel> findById(long counselId) {
        return counselRepository.findById(counselId);
    }

    @Override
    public Optional<Counsel> findByUsernameId(String username, long counselId) {
        return counselRepository.findByUserAndId(userRepository.getByUsername(username), counselId);
    }

    @Override
    public List<Counsel> findAllByKeyword(String searchKeyword) {
        return counselRepository.findAllByContentsContaining(searchKeyword);
    }


    @Override
    public Boolean findCustomerIfManager(long counselId, String username) {
        Customer customer = counselRepository.findById(counselId)
                .orElseThrow(()->
                        new FindException(DatabaseErrorCode.CUSTOMER_NOT_FOUND.name()))
                .getCustomer();
        log.info(username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->
                        new FindException(DatabaseErrorCode.USER_NOT_FOUND.name()));

        if(!customer.getManager().getUsername().equals(user.getUsername()))
            return false;

        return true;
    }

    @Override
    public List<CounselResponseDto> updateCounsel(long counselId, CounselUpdateDto counselUpdateDto) {
        Counsel originalCounsel = counselRepository.findById(counselId)
                .orElseThrow(()->
                        new FindException(DatabaseErrorCode.CUSTOMER_NOT_FOUND.name()));
        Customer customer = customerRepository.findById(counselUpdateDto.getCustomerId()).orElseThrow(()->
                new FindException("Customer "+ ErrorCode.RESOURCE_NOT_FOUND));
        try{
            originalCounsel  = counselRepository.save(originalCounsel.updateModified(counselUpdateDto.toEntity()));
            customer.setCounselStatus(originalCounsel.getStatus());

        } catch (Exception e){
            log.info("Counsel Update Failed");
            throw e;
        }

        return counselRepository.findAllByCustomer(originalCounsel.getCustomer())
                .stream().map(counsel -> wrapCounselResponseDto(counsel))
                .collect(Collectors.toList());
    }

    @Override
    public List<CounselResponseDto> deleteCounsel(long counselId) {
        Counsel counsel = counselRepository.findById(counselId).orElseThrow(()->new FindException("Counsel "+ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        Customer customer = counsel.getCustomer();

        try{
            counselRepository.delete(counsel);
        } catch(Exception e){
            throw e;
        }
        return counselRepository.findAllByCustomer(customer).stream().map(counsel1 -> wrapCounselResponseDto(counsel)).collect(Collectors.toList());
    }

}
