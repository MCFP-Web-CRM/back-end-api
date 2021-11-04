package com.mcfuturepartners.crm.api.goal.controller;

import com.mcfuturepartners.crm.api.goal.entity.Goal;
import com.mcfuturepartners.crm.api.goal.service.GoalService;
import com.mcfuturepartners.crm.api.revenue.entity.Revenue;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/goal")
@RequiredArgsConstructor
public class GoalController {
    private final GoalService goalService;

    @GetMapping
    @ApiOperation(value = "회사 전체 매출 조회 api", notes = "month=6(변경 가능) querystring으로 원하는 달만큼 가져올 수 있음")
    public ResponseEntity<List<Goal>> getCompanyGoal(@RequestParam("month") Integer month){
        return new ResponseEntity<>(goalService.getCompanyLatestGoalByMonth(month), HttpStatus.OK);
    }
}
