package com.mcfuturepartners.crm.api.counsel.service;

import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.dto.CounselResponseDto;
import com.mcfuturepartners.crm.api.counsel.dto.CounselUpdateDto;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CounselService {
    CounselResponseDto wrapCounselResponseDto(Counsel counsel);
    Boolean findCustomerIfManager(long counselId, String username);

    List<CounselResponseDto> saveCounsel(CounselDto counselDto);

    List<CounselResponseDto> findAll();
    List<CounselResponseDto> findAllByUsername(String username);
    List<Counsel> findAllByUserId(long userId);

    Optional<Counsel> findById(long counselId);
    Optional<Counsel> findByUsernameId(String username, long counselId);
    List<Counsel> findAllByKeyword(String searchKeyword);

    List<CounselResponseDto> updateCounsel(long counselId, CounselUpdateDto counsel);
    List<CounselResponseDto> deleteCounsel(long counselId);
}
