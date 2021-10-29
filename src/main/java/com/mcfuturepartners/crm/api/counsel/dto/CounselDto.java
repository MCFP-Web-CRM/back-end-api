package com.mcfuturepartners.crm.api.counsel.dto;

import com.mcfuturepartners.crm.api.counsel.entity.Counsel;
import com.mcfuturepartners.crm.api.counsel.entity.CounselStatus;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.repository.UserRepository;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;

@Data
@Component
@NoArgsConstructor
public class CounselDto {
    @ApiModelProperty(position = 0) private long id;
    @ApiModelProperty(position = 1) private long customerId;
    @ApiModelProperty(position = 2) private String status;
    @ApiModelProperty(position = 3) private String contents;
    private String username;

    public Counsel toEntity(){
       return Counsel.builder()
               .status(Arrays.stream(CounselStatus.values()).filter(counselStatus -> counselStatus.getStatus().equals(status)).findFirst().get())
                .contents(contents)
                .regDate(LocalDateTime.now())
                .build();
    }


}
