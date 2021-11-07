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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/util/file/customer")
@RequiredArgsConstructor
@Slf4j
public class FileCustomerController {
    private final CustomerService customerService;

    @RequestMapping(value = "/upload",method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveCustomer(@RequestPart MultipartFile customerDocument) throws IOException {
        List<Customer> customerList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(customerDocument.getInputStream());
        Sheet worksheet = workbook.getSheetAt(0);
        for (int i = 1 ; i < worksheet.getPhysicalNumberOfRows(); i++){
            Row row = worksheet.getRow(i);
            //row.getCell(0).setCellType(CellType.STRING);
            if(row==null) continue;

            Cell c1 = row.getCell(0);
            Cell c2 = row.getCell(1);

            if(c1 == null || c1.getCellType() == CellType.BLANK){
                continue;
            }
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
            customerList.add(Customer.builder().phone(phone).name(name).build());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
