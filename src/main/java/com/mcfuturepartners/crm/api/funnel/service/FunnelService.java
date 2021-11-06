package com.mcfuturepartners.crm.api.funnel.service;

import com.mcfuturepartners.crm.api.funnel.dto.FunnelCreateDto;
import com.mcfuturepartners.crm.api.funnel.dto.FunnelResponseDto;
import com.mcfuturepartners.crm.api.funnel.entity.Funnel;

import java.util.List;

public interface FunnelService {
    List<FunnelResponseDto> getFunnelList();
    List<FunnelResponseDto> addFunnel (FunnelCreateDto funnelCreateDto);
    List<FunnelResponseDto> updateFunnel (Long funnelId, FunnelCreateDto funnelCreateDto);
    List<FunnelResponseDto> deleteFunnel(Long id);
}
