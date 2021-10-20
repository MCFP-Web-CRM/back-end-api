package com.mcfuturepartners.crm.api.counsel.controller;

import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.counsel.repository.CounselRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/counsel")
@RequiredArgsConstructor
public class CounselController {
    private final CounselRepository counselRepository; //이후 service로 변경


    @GetMapping("/health-check")
    public ResponseEntity<String> counseHealthCheck(){
        return new ResponseEntity<>("counsel Health Check", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> saveCounsel(CounselDto counselDto){
        counselRepository.save(counselDto.toEntity());
        return new ResponseEntity<>("save Succeeded",HttpStatus.CREATED);
    }

}
