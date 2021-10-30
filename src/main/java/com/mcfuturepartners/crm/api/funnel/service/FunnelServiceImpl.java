package com.mcfuturepartners.crm.api.funnel.service;

import com.mcfuturepartners.crm.api.funnel.dto.FunnelCreateDto;
import com.mcfuturepartners.crm.api.funnel.dto.FunnelResponseDto;
import com.mcfuturepartners.crm.api.funnel.entity.Funnel;
import com.mcfuturepartners.crm.api.funnel.repository.FunnelRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FunnelServiceImpl implements FunnelService{
    private final FunnelRepository funnelRepository;
    private final ModelMapper modelMapper;
    @Override
    public List<FunnelResponseDto> getFunnelList() {
        return funnelRepository.findAll().stream().map(funnel -> modelMapper.map(funnel, FunnelResponseDto.class)).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<FunnelResponseDto> addFunnel (FunnelCreateDto funnelCreateDto) {
        funnelRepository.save(funnelCreateDto.toEntity());
        return getFunnelList();
    }

    @Transactional
    @Override
    public List<FunnelResponseDto> deleteFunnel(Long id) {
        funnelRepository.deleteById(id);
        return getFunnelList();
    }
}
