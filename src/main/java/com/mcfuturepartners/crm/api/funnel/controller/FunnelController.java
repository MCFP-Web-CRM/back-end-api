package com.mcfuturepartners.crm.api.funnel.controller;

import com.mcfuturepartners.crm.api.customer.dto.CustomerRegisterDto;
import com.mcfuturepartners.crm.api.funnel.dto.FunnelCreateDto;
import com.mcfuturepartners.crm.api.funnel.dto.FunnelResponseDto;
import com.mcfuturepartners.crm.api.funnel.service.FunnelService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/funnel")
public class FunnelController {
    private final FunnelService funnelService;

    @GetMapping
    @ApiOperation(value = "유입경로 전체 확인 리스트", notes = "전체유입 경로 리스트 확인")
    public ResponseEntity<List<FunnelResponseDto>> getFunnelList(){
        return new ResponseEntity<>(funnelService.getFunnelList(), HttpStatus.OK);
    }
}
