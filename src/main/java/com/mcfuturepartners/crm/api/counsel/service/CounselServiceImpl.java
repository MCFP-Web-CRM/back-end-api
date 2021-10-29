package com.mcfuturepartners.crm.api.counsel.service;

import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.exception.CounselException;
import com.mcfuturepartners.crm.api.counsel.repository.CounselRepository;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    public List<CounselDto> saveCounsel(CounselDto counselDto) {
        Counsel counsel = counselDto.toEntity();
        Customer customer = customerRepository.getById(counselDto.getCustomerId());
        counsel.setCustomer(customer);
        counsel.setUser(userRepository.getByUsername(counselDto.getUsername()));
        counselRepository.save(counsel);


        return counselRepository.findAllByCustomer(customer).stream().map(counsel1 -> modelMapper.map(counsel1,CounselDto.class)).collect(Collectors.toList());
    }

    @Override
    public List<Counsel> findAll() {
        return counselRepository.findAll();
    }

    @Override
    public List<Counsel> findAllByUsername(String username) {
        return counselRepository.findAllByUser(userRepository.findByUsername(username).get());
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
    public List<Counsel> findAllByUsernameKeyword(String username, String searchKeyword) {
        return counselRepository.findAllByUserAndContentsContaining(userRepository.getByUsername(username),searchKeyword);
    }

    @Override
    public List<CounselDto> updateCounsel(long counselId, CounselDto counselDto) {
        Customer customer = counselRepository.findById(counselId).get().getCustomer();



        Counsel updatedCounsel = counselDto.toEntity();
        updatedCounsel.setCustomer(customerRepository.getById(counselDto.getCustomerId()));
        updatedCounsel.setId(counselId);
        updatedCounsel.setUser(userRepository.getByUsername(counselDto.getUsername()));

        try{
            counselRepository.save(updatedCounsel);
            return counselRepository.findAllByCustomer(customer).stream().map(counsel1 -> modelMapper.map(counsel1,CounselDto.class)).collect(Collectors.toList());

        } catch (Exception e){
            throw e;
        }

    }

    @Override
    public List<CounselDto> deleteCounsel(long counselId) {
        Customer customer = counselRepository.findById(counselId).orElseThrow().getCustomer();
        try{
            counselRepository.delete(counselRepository.findById(counselId).orElseThrow());
            return counselRepository.findAllByCustomer(customer).stream().map(counsel1 -> modelMapper.map(counsel1,CounselDto.class)).collect(Collectors.toList());

        } catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<CounselDto> deleteCounselByUsername(String username, long counselId) throws CounselException{
        Customer customer = counselRepository.findById(counselId).get().getCustomer();

        if(counselRepository.findById(counselId).get().getUser().getUsername().equals(username)){

            return deleteCounsel(counselId);

        } else{
            throw new CounselException(ErrorCode.UNAUTHORIZED.getCode());
        }

    }
}