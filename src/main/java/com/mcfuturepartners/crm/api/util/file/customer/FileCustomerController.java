package com.mcfuturepartners.crm.api.util.file.customer;

import com.mcfuturepartners.crm.api.customer.dto.CustomerRegisterDto;
import com.mcfuturepartners.crm.api.customer.entity.Customer;
import com.mcfuturepartners.crm.api.customer.repository.CustomerRepository;
import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.service.UserService;
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
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

@RestController
@RequestMapping(path = "/util/file/customer")
@RequiredArgsConstructor
@Slf4j
public class FileCustomerController {
    private final CustomerService customerService;
    private final UserService userService;

    @RequestMapping(value = "/upload",method = RequestMethod.POST, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<String> saveCustomer(@ModelAttribute FileInputDto fileInputDto) throws IOException {
        List<Customer> customerList = new ArrayList<>();
        HashMap<String, Customer> customerBook = new HashMap<>();
        Workbook workbook = new XSSFWorkbook(fileInputDto.getCustomerDocument().getInputStream());
        Sheet worksheet = workbook.getSheetAt(0);
        String pattern1 =  "^\\d{10,11}$";
        String pattern2 = "^\\d{3}-\\d{3,4}-\\d{4}$";

        for (int i = 0 ; i < worksheet.getPhysicalNumberOfRows(); i++){
            Row row = worksheet.getRow(i);
            //row.getCell(0).setCellType(CellType.STRING);
            if(row==null) continue;

            Cell c1 = row.getCell(0);
            Cell c2 = row.getCell(1);
            Cell c3 = row.getCell(2);
            if(c1 == null || c1.getCellType() == CellType.BLANK){
                continue;
            }

            if(c2 == null || c2.getCellType() == CellType.BLANK) {
               c2.setCellValue("");
            }

//            if(c3 == null || c3.getCellType() == CellType.BLANK){
//                c3.setCellValue("");
//            }
            c1.setCellType(CellType.STRING);
            c2.setCellType(CellType.STRING);
            //c3.setCellType(CellType.STRING);

            if(row.getCell(1).getStringCellValue().equals("미입력")||row.getCell(1).getStringCellValue().equals("익명")){
                row.getCell(1).setCellValue("");
            }
            String phone = row.getCell(0).getStringCellValue();//

            if(!Pattern.matches(pattern1,phone)&&!Pattern.matches(pattern2,phone)){
                continue;
            }
            if(Pattern.matches(pattern2,phone)){
                phone = phone.replace("-","");
            }
            String name = row.getCell(1).getStringCellValue();

            User manager = null;
            if(customerBook.containsKey(phone) || customerService.checkCustomerExists(phone)) {
                continue;
            }
            if(c3 != null){
                String managerName = row.getCell(2).getStringCellValue();
                manager = userService.getUserByName(managerName);
            }
            customerBook.put(phone,Customer.builder().phone(phone).manager(manager).name(name).regDate(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime()).build());
        }
        customerBook.forEach((s, customer) -> customerService.save(customer));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
