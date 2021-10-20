package com.mcfuturepartners.crm.api.counsel.service;

import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.repository.CounselRepository;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CounselServiceImpl implements CounselService {
    private final CounselRepository counselRepository;
    private final UserRepository userRepository;
    @Override
    public long saveCounsel(String username, CounselDto counselDto) {
        counselDto.setUser(userRepository.getByUsername(username));
        return counselRepository.save(counselDto.toEntity()).getId();
    }
}
