package com.mcfuturepartners.crm.api.counsel.service;

import com.mcfuturepartners.crm.api.counsel.dto.CounselDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CounselServiceImplTest {
    @Autowired
    private CounselService counselService;
    @Autowired
    private CustomerService customerService;

    @Test @DisplayName("counsel save test")
    void save(){
        customerService.save(Customer.builder()
                        .name("박재현")
                        .birth("19920309")
                        .email("test@test.com")
                        .phone("01091321231")
                        .sex("MALE")
                        .regDate(LocalDateTime.now())
                        .funnel("카카오톡")
                        .manager("")
                .build());
        CounselDto counselDto = new CounselDto();
        counselDto.setCustomerId(1);
        counselDto.setProductId("22");
        counselDto.setStatus("가망 상");
        counselDto.setContents("12123");
        counselDto.setUsername("test123");
        counselService.saveCounsel(counselDto);
        Assertions.assertThat(counselService.findById(1).get().getCustomer().getId().equals(1));
    }

}