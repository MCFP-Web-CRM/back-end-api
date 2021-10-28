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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
@RequiredArgsConstructor
public class CounselServiceImpl implements CounselService {
    private final CounselRepository counselRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    @Override
    public Counsel saveCounsel(CounselDto counselDto) {
        Counsel counsel = counselDto.toEntity();

        counsel.setUser(userRepository.getByUsername(counselDto.getUsername()));

        return counselRepository.save(counsel);
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
    public String updateCounsel(long counselId, CounselDto counselDto) {

        if(!counselDto.getUsername().equals(counselRepository.findById(counselId).get().getUser().getUsername()))
            return ErrorCode.UNAUTHORIZED.getMsg();

        Counsel updatedCounsel = counselDto.toEntity();
        updatedCounsel.setCustomer(customerRepository.getById(updatedCounsel.getCustomer().getId()));
        updatedCounsel.setId(counselId);
        updatedCounsel.setUser(userRepository.getByUsername(counselDto.getUsername()));

        try{
            counselRepository.save(updatedCounsel);
            return "successfully done";
        } catch (Exception e){
            throw e;
        }

    }

    @Override
    public void deleteCounsel(long counselId) {
        try{
            log.info(counselRepository.findById(counselId).get().toString());
            counselRepository.delete(counselRepository.findById(counselId).get());
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCounselByUsername(String username, long counselId) throws CounselException{
        if(counselRepository.findById(counselId).get().getUser().getUsername().equals(username)){
            deleteCounsel(counselId);
        } else{
            throw new CounselException(ErrorCode.UNAUTHORIZED.getCode());
        }

    }
}
