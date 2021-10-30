package com.mcfuturepartners.crm.api.notice.entity;

import com.mcfuturepartners.crm.api.customer.dto.CustomerUpdateDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@Table(name = "notice")
@AllArgsConstructor
@NoArgsConstructor
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "NOTICE_ID")
    private Long noticeId;

    @Column(name="REGISTER_DATE")
    private LocalDateTime regDate;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "CONTENTS")
    private String contents;

    public Notice writeNow(){
        regDate = LocalDateTime.now();
        return this;
    }
    public Notice modifyUpdated(Notice notice){
        if(ObjectUtils.isEmpty(notice.title)&&ObjectUtils.isEmpty(notice.contents)){
            return this;
        }
        if(!ObjectUtils.isEmpty(notice.title)){
            this.title = title;
        }
        if(ObjectUtils.isEmpty(notice.contents)){
            this.contents = contents;
        }
        return writeNow();
    }
}
