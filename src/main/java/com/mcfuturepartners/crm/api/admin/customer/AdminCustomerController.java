package com.mcfuturepartners.crm.api.admin.customer;

import com.mcfuturepartners.crm.api.customer.service.CustomerService;
import com.mcfuturepartners.crm.api.exception.FindException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @PutMapping(path = "/manager/change")
    @ApiOperation(value = "담당자 변경 API (고객관리)",notes = "고객관리 탭 체크")
    public ResponseEntity<String> changeManagerInCustomerRelations(@RequestBody CheckManagerChangeDto checkManagerChangeDto){
        try{
            customerService.changeMangagerOfCheckedCustomers(checkManagerChangeDto);
        }catch (FindException findException){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Update Success",HttpStatus.OK);
    }
    @DeleteMapping
    public ResponseEntity<Void> deleteCustomers(@RequestParam(value = "customer-id") List<Long> customerIds){
        try{
            customerService.deleteMultipleCustomers(customerIds);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
