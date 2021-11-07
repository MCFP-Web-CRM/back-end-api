package com.mcfuturepartners.crm.api.util.file.customer;

import com.mcfuturepartners.crm.api.customer.dto.CustomerRegisterDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
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
    private final CustomerRepository customerRepository;

    @RequestMapping(value = "/upload",method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveCustomer(@RequestPart MultipartFile customerDocument) throws IOException {
        List<Customer> customerList = new ArrayList<>();
        Workbook workbook = new XSSFWorkbook(customerDocument.getInputStream());
        Sheet worksheet = workbook.getSheetAt(0);
        for (int i = 1 ; i < worksheet.getPhysicalNumberOfRows(); i++){
            if(worksheet.getRow(i)==null) continue;

            if(worksheet.getRow(i).getCell(0) == null || worksheet.getRow(i).getCell(0).getCellType() == CellType.BLANK){
                continue;
            }
            if(worksheet.getRow(0).getCell(1) == null||worksheet.getRow(0).getCell(1).getCellType() == CellType.BLANK) {
                worksheet.getRow(0).getCell(1).setCellValue("");
            }
            worksheet.getRow(i).getCell(0).setCellType(CellType.STRING);
            worksheet.getRow(0).getCell(1).setCellType(CellType.STRING);
            if(worksheet.getRow(i).getCell(1).getStringCellValue().equals("미입력")||worksheet.getRow(i).getCell(1).getStringCellValue().equals("익명")){
                worksheet.getRow(i).getCell(1).setCellValue("");
            }
            customerRepository.save(Customer.builder().phone(worksheet.getRow(i).getCell(0).getStringCellValue().replace("-","")).name(worksheet.getRow(i).getCell(1).getStringCellValue()).build());
        }
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
