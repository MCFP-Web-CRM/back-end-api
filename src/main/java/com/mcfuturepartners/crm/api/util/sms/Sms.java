package com.mcfuturepartners.crm.api.util.sms;

import lombok.Data;

import java.util.List;
@Data
public class Sms {
    private List<String> receiverPhone;
    private String content;
}
