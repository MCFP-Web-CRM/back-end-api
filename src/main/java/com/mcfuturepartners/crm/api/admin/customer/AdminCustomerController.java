package com.mcfuturepartners.crm.api.admin.customer;

import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import com.mcfuturepartners.crm.api.exception.FindException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/admin/customer")
@RequiredArgsConstructor
public class AdminCustomerController {
    private final CustomerService customerService;


    @PostMapping(path = "/manager/change")
    @ApiOperation(value = "담당자 변경 API", notes = "담당자 이관")
    public ResponseEntity<String> changeManager(@RequestBody ManagerChangeDto managerChangeDto){
        try{
            customerService.changeAllCustomersManager(managerChangeDto);
        }catch (FindException findException){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Update Success",HttpStatus.OK);
    }
}
