package com.mcfuturepartners.crm.api.counsel.service;

import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CounselService {
    long saveCounsel(String username, CounselDto counselDto);
    List<Counsel> findAll();
    List<Counsel> findAllByUsername(String username);
    List<Counsel> findAllByUserId(long userId);

    Optional<Counsel> findById(long counselId);
    Optional<Counsel> findByUsernameId(String username, long counselId);
    List<Counsel> findAllByKeyword(String searchKeyword);
    List<Counsel> findAllByUsernameKeyword(String username, String searchKeyword);
    Counsel updateCounsel(long counselId, CounselDto counsel);
    void deleteCounsel(long counselId);
    void deleteCounselByUsername(String username, long counselId);
}
