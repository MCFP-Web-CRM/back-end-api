package com.mcfuturepartners.crm.api.counsel.service;

import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CounselService {
    List<CounselDto> saveCounsel(CounselDto counselDto);
    List<Counsel> findAll();
    List<Counsel> findAllByUsername(String username);
    List<Counsel> findAllByUserId(long userId);

    Optional<Counsel> findById(long counselId);
    Optional<Counsel> findByUsernameId(String username, long counselId);
    List<Counsel> findAllByKeyword(String searchKeyword);
    List<Counsel> findAllByUsernameKeyword(String username, String searchKeyword);
    List<CounselDto> updateCounsel(long counselId, CounselDto counsel);
    List<CounselDto> deleteCounsel(long counselId);
    List<CounselDto> deleteCounselByUsername(String username, long counselId);
}
