package com.mcfuturepartners.crm.api.revenue.controller;

import com.mcfuturepartners.crm.api.order.service.OrderService;
import com.mcfuturepartners.crm.api.revenue.dto.ProductMonthlyRequest;
import com.mcfuturepartners.crm.api.revenue.dto.ProductMonthlyRevenue;
import com.mcfuturepartners.crm.api.revenue.dto.UserMonthlyRequest;
import com.mcfuturepartners.crm.api.revenue.dto.UserMonthlyRevenue;
import com.mcfuturepartners.crm.api.revenue.entity.Revenue;
import com.mcfuturepartners.crm.api.revenue.service.RevenueService;
import com.mcfuturepartners.crm.api.user.dto.UserRevenueDto;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/revenue")
@RequiredArgsConstructor
@Slf4j
public class RevenueController {
    private final RevenueService revenueService;
    @GetMapping
    @ApiOperation(value = "회사 전체 매출 조회 api", notes = "month=6(변경 가능) querystring으로 원하는 달만큼 가져올 수 있음")
    public ResponseEntity<List<Revenue>> getTotalRevenue(@RequestParam("month") Integer month){
        return new ResponseEntity<>(revenueService.getCompanyLatestRevenueByMonth(month), HttpStatus.OK);
    }

    @GetMapping(path = "/user")
    @ApiOperation(value = "사원별 매출 조회", notes = "month=6(변경 가능) querystring으로 원하는 달만큼 가져올 수 있고, user-id=1,2,3 이런식으로 원하는 id를 추가해주면 그 사원의 매출을 가져올 수 있음")
    public ResponseEntity<List<List<UserMonthlyRevenue>>> getUserRevenue(@RequestParam("month") Integer month,
                                                                         @RequestParam("user-id") List<Long> userIds){

        return new ResponseEntity<>(revenueService.getUsersLatestRevenueByMonth(UserMonthlyRequest.builder().month(month).userIds(userIds).build()), HttpStatus.OK);
    }
    @GetMapping(path = "/product")
    @ApiOperation(value = "상품별 매출 조회", notes = "month=6(변경 가능) querystring으로 원하는 달만큼 가져올 수 있고, product-id=1,2,3 이런식으로 원하는 id를 추가해주면 그 상품의 매출을 가져올 수 있음")
    public ResponseEntity<List<List<ProductMonthlyRevenue>>> getProductRevenue(@RequestParam("month") Integer month,
                                                                               @RequestParam("product-id") List<Long> productIds){

        return new ResponseEntity<>(revenueService.getProductsLatestRevenueByMonth(ProductMonthlyRequest.builder().month(month).productIds(productIds).build()), HttpStatus.OK);
    }
}
