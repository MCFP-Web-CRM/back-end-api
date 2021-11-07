package com.mcfuturepartners.crm.api.funnel.service;

import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
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

    @Override
    public List<FunnelResponseDto> updateFunnel(Long funnelId, FunnelCreateDto funnelCreateDto) {
        Funnel funnel = funnelRepository.findById(funnelId).orElseThrow(()->new FindException("Funnel "+ErrorCode.RESOURCE_NOT_FOUND));
        funnel.setFunnelName(funnelCreateDto.getFunnelName());
        funnelRepository.save(funnel);
        return getFunnelList();
    }


    @Transactional
    @Override
    public List<FunnelResponseDto> deleteFunnel(Long id) {
        Funnel funnel = funnelRepository.findById(id).orElseThrow(()->new FindException("Funnel "+ ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        funnel.removeConnectionWithCustomers();
        funnelRepository.save(funnel);

        try{
            funnelRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
        return getFunnelList();
    }
}
