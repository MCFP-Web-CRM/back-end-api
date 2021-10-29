package com.mcfuturepartners.crm.api.counsel.service;

import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.exception.CounselException;
import com.mcfuturepartners.crm.api.counsel.repository.CounselRepository;
import com.mcfuturepartners.crm.api.exception.DatabaseErrorCode;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.dialect.Database;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public CounselDto wrapCounselDto(Counsel counsel) {
        return modelMapper.map(counsel, CounselDto.class);
    }

    @Override
    public List<CounselDto> saveCounsel(CounselDto counselDto) {
        Counsel counsel = counselDto.toEntity();
        Customer customer = customerRepository.getById(counselDto.getCustomerId());
        counsel.setCustomer(customer);
        counsel.setUser(userRepository.getByUsername(counselDto.getUsername()));
        counselRepository.save(counsel);


        return counselRepository.findAllByCustomer(customer).stream().map(counsel1 -> wrapCounselDto(counsel1)).collect(Collectors.toList());
    }
    @Override
    public List<CounselDto> findAll() {
        return counselRepository.findAll().stream().map(counsel -> wrapCounselDto(counsel)).collect(Collectors.toList());
    }

    @Override
    public List<CounselDto> findAllByUsername(String username) {
        return counselRepository.findAllByUser(userRepository.findByUsername(username).orElseThrow(()-> new FindException(DatabaseErrorCode.USER_NOT_FOUND.name()))).stream().map(counsel -> wrapCounselDto(counsel)).collect(Collectors.toList());
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
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->
                        new FindException(DatabaseErrorCode.USER_NOT_FOUND.name()));

        if(!customer.getManager().getUsername().equals(user.getUsername()))
            return false;

        return false;
    }

    @Override
    public List<CounselDto> updateCounsel(long counselId, CounselDto counselDto) {
        Customer customer = counselRepository.findById(counselId)
                .orElseThrow(()->
                        new FindException(DatabaseErrorCode.CUSTOMER_NOT_FOUND.name()))
                .getCustomer();

        Counsel updatedCounsel = counselDto.toEntity();
        updatedCounsel.setCustomer(customer);
        updatedCounsel.setId(counselId);
        updatedCounsel.setUser(userRepository.getByUsername(counselDto.getUsername()));

        try{
            counselRepository.save(updatedCounsel);
        } catch (Exception e){
            log.info("Counsel Update Failed");
        }
        return counselRepository.findAllByCustomer(customer)
                .stream().map(counsel1 -> wrapCounselDto(counsel1))
                .collect(Collectors.toList());
    }

    @Override
    public List<CounselDto> deleteCounsel(long counselId) {
        Customer customer = counselRepository.findById(counselId).orElseThrow().getCustomer();
        try{
            counselRepository.delete(counselRepository.findById(counselId).orElseThrow(()-> new FindException(DatabaseErrorCode.COUNSEL_NOT_FOUND.name())));
            return counselRepository.findAllByCustomer(customer).stream().map(counsel1 -> wrapCounselDto(counsel1)).collect(Collectors.toList());

        } catch(Exception e){
            log.info("Counsel Delete Failed");
        }
        return counselRepository.findAllByCustomer(customer).stream().map(counsel1 -> wrapCounselDto(counsel1)).collect(Collectors.toList());
    }

}
