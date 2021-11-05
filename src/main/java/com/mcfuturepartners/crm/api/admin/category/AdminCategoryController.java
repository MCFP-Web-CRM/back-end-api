package com.mcfuturepartners.crm.api.admin.category;

import com.mcfuturepartners.crm.api.category.dto.CategoryDto;
import com.mcfuturepartners.crm.api.category.entity.Category;
import com.mcfuturepartners.crm.api.category.service.CategoryService;
import com.mcfuturepartners.crm.api.exception.FindException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/admin/category")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;
    @PostMapping
    @ApiOperation(value = "고객 그룹 추가", notes = "(예외 처리 필요)")
    public ResponseEntity<String> createCustomerCategory(@RequestBody CategoryDto categoryDto){
        return new ResponseEntity<>(categoryService.createCategory(categoryDto), HttpStatus.CREATED);
    }
    @DeleteMapping(path = "/{category-id}")
    @ApiOperation(value = "고객 그룹 추가", notes = "(예외 처리 필요)")
    public ResponseEntity<Void> deleteCustomerCategory(@PathVariable(name = "category-id") Long categoryId){
        try{
            categoryService.deleteCategory(categoryId);
        }catch(FindException findException){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping(path = "/{category-id}")
    @ApiOperation(value = "고객 그룹 추가", notes = "(예외 처리 필요)")
    public ResponseEntity<CategoryDto> deleteCustomerCategory(@PathVariable(name = "category-id") Long categoryId,
                                                       @RequestBody CategoryDto categoryDto) {

        CategoryDto category;
        try{
            category = categoryService.updateCategory(categoryId, categoryDto);
        } catch (FindException findException){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
}
