package com.mcfuturepartners.crm.api.counsel.service;

import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.dto.CounselUpdateDto;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CounselService {
    CounselDto wrapCounselDto(Counsel counsel);
    Boolean findCustomerIfManager(long counselId, String username);

    List<CounselDto> saveCounsel(CounselDto counselDto);

    List<CounselDto> findAll();
    List<CounselDto> findAllByUsername(String username);
    List<Counsel> findAllByUserId(long userId);

    Optional<Counsel> findById(long counselId);
    Optional<Counsel> findByUsernameId(String username, long counselId);
    List<Counsel> findAllByKeyword(String searchKeyword);

    List<CounselDto> updateCounsel(long counselId, CounselUpdateDto counsel);
    List<CounselDto> deleteCounsel(long counselId);
}
