package com.mcfuturepartners.crm.api.admin.user;

import com.mcfuturepartners.crm.api.department.repository.DepartmentRepository;
import com.mcfuturepartners.crm.api.department.service.DepartmentService;
import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.exception.LoginException;
import com.mcfuturepartners.crm.api.user.dto.UserDto;
import com.mcfuturepartners.crm.api.user.dto.UserResponseDto;
import com.mcfuturepartners.crm.api.user.entity.User;
import com.mcfuturepartners.crm.api.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/user")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;
    private final DepartmentService departmentService;
    private final DepartmentRepository departmentRepository;

    @PostMapping
    @ApiOperation(value = "사원 계정 추가", notes = "부서가 지정되지 않았을 경우 null로 지정")
    public ResponseEntity<UserResponseDto> signup(@RequestBody UserDto userDto){
        //service레이어에서 처리할 수 있도록 처리
        UserResponseDto userResponseDto;

        try{
            userResponseDto = userService.signup(userDto);
        }catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch(LoginException loginException){
            loginException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(userResponseDto, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/{userid}")
    @ApiOperation(value = "사원 계정 삭제 api", notes = "삭제 시 담당하던 고객들을 배정 없음으로 돌리는 로직 추가적으로 필요")
    public ResponseEntity<String> deleteUser(@PathVariable("userid") long userId){
        if(userService.deleteUser(userId).isEmpty()){
            return new ResponseEntity<>("not found", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("deleted",HttpStatus.OK);
    }
}
