package com.mcfuturepartners.crm.api.counsel.service;

import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import org.springframework.stereotype.Service;

@Service
public interface CounselService {
    long saveCounsel(String username, CounselDto counselDto);
}
