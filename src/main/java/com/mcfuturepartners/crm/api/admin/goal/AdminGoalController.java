package com.mcfuturepartners.crm.api.admin.goal;

import com.mcfuturepartners.crm.api.goal.entity.Goal;
import com.mcfuturepartners.crm.api.goal.service.GoalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/goal")
public class AdminGoalController {
    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<Goal> setMonthlyGoal(@RequestBody Goal goal){
        return new ResponseEntity<>(goalService.setMonthlyGoal(goal), HttpStatus.OK);
    }

    @PutMapping(name = "/{goal-id}")
    public ResponseEntity<String> updateMonthlyGoal(@RequestBody Goal goal,
                                                    @PathVariable(name = "goal-id") Long goalId){
        return new ResponseEntity<>("update Successful", HttpStatus.OK);
    }

    @DeleteMapping(name = "/{goal-id}")
    public ResponseEntity<String> deleteMonthlyGoal(@PathVariable(name = "goal-id") Long goalId){
        return new ResponseEntity<>("delete Successful", HttpStatus.OK);
    }
}
