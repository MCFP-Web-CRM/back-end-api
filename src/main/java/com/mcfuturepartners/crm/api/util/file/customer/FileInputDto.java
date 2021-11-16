package com.mcfuturepartners.crm.api.util.file.customer;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class FileInputDto {
    private MultipartFile customerDocument;
}
