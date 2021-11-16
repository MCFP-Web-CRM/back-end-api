package com.mcfuturepartners.crm.api.util.file.customer;

import com.mcfuturepartners.crm.api.customer.dto.CustomerRegisterDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/util/file/customer")
@RequiredArgsConstructor
@Slf4j
public class FileCustomerController {
    private final CustomerService customerService;

    @RequestMapping(value = "/upload",method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveCustomer(@ModelAttribute FileInputDto fileInputDto) throws IOException {
        List<Customer> customerList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(fileInputDto.getCustomerDocument().getInputStream());
        Sheet worksheet = workbook.getSheetAt(0);
        for (int i = 1 ; i < worksheet.getPhysicalNumberOfRows(); i++){
            Row row = worksheet.getRow(i);
            //row.getCell(0).setCellType(CellType.STRING);
            log.info(Integer.toString(i));
            if(row==null) continue;

            Cell c1 = row.getCell(0);
            Cell c2 = row.getCell(1);

            if(c1 == null || c1.getCellType() == CellType.BLANK){
                continue;
            }
            log.info(c1.toString()+" "+c2.toString());
            if(c2 == null||c2.getCellType() == CellType.BLANK) {
               c2.setCellValue("");
            }
            c1.setCellType(CellType.STRING);
            c2.setCellType(CellType.STRING);
            if(row.getCell(1).getStringCellValue().equals("미입력")||row.getCell(1).getStringCellValue().equals("익명")){
                row.getCell(1).setCellValue("");
            }
            String phone = row.getCell(0).getStringCellValue().replace("-","");
            String name = row.getCell(1).getStringCellValue();
            customerList.add(Customer.builder().phone(phone).name(name).regDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()).build());
        }
        log.info(customerService.saveAll(customerList).toString());
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
