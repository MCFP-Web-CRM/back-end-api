package com.mcfuturepartners.crm.api.admin.funnel;

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
@RequestMapping(value = "/admin/funnel")
@RequiredArgsConstructor
public class AdminFunnelController {
    private final FunnelService funnelService;
    @PostMapping
    @ApiOperation(value = "유입경로 항목 추가", notes = "유입경로 추가, 추가 후 정상 응답 시 전체 유입 경로 반환")
    public ResponseEntity<List<FunnelResponseDto>> addFunnel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                             @RequestBody FunnelCreateDto funnelCreateDto){
        try{
            funnelService.addFunnel(funnelCreateDto);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(funnelService.getFunnelList(), HttpStatus.OK);
    }


    @PutMapping(path = "/{funnel-id}")
    @ApiOperation(value = "유입경로 항목 수정", notes = "유입경로 수정, 수정 후 정상 응답 시 전체 유입 경로 반환")
    public ResponseEntity<List<FunnelResponseDto>> updateFunnel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                                @PathVariable(name = "funnel-id") Long funnelId,
                                                                @RequestBody FunnelCreateDto funnelCreateDto){
        try{
            funnelService.updateFunnel(funnelId,funnelCreateDto);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(funnelService.getFunnelList(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{funnel-id}")
    @ApiOperation(value = "유입경로 항목 삭제", notes = "유입경로 삭제, 삭제 후 정상 응답 시 전체 유입 경로 반환")
    public ResponseEntity<List<FunnelResponseDto>> deleteFunnel(@RequestHeader(HttpHeaders.AUTHORIZATION)String bearerToken,
                                                                @PathVariable(name = "funnel-id") Long funnelId){
        try{
            funnelService.deleteFunnel(funnelId);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(funnelService.getFunnelList(), HttpStatus.OK);
    }
}
