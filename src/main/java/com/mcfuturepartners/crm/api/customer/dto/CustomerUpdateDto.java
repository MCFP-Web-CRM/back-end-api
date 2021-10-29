package com.mcfuturepartners.crm.api.customer.dto;

import com.mcfuturepartners.crm.api.customer.entity.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerUpdateDto {
    private long id;
    private String name;
    private String birth;
    private String email;
    private String phone;
    private String sex;
    private String funnel;
    private String specialNote;
    private long categoryId;
    private long managerUserId;

    public Customer toEntity(){
        return Customer.builder()
                .id(id)
                .name(name)
                .birth(birth)
                .email(email)
                .sex(sex)
                .funnel(funnel)
                .specialNote(specialNote)
                .phone(phone)
                .build();
    }
}
