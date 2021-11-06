package com.mcfuturepartners.crm.api.admin.goal;

import com.mcfuturepartners.crm.api.exception.FindException;
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
        try {
           goal = goalService.setMonthlyGoal(goal);
        }catch (FindException findException){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(goal, HttpStatus.OK);
    }

    @PutMapping(path = "/{goal-id}")
    public ResponseEntity<Goal> updateMonthlyGoal(@RequestBody Goal goal,
                                                  @PathVariable(name = "goal-id") Long goalId){
        try{
            goal = goalService.updateMonthlyGoal(goalId, goal);
            if(goal==null){
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }catch (FindException findException){
            findException.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(goal, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{goal-id}")
    public ResponseEntity<String> deleteMonthlyGoal(@PathVariable(name = "goal-id") Long goalId){

        try{
            goalService.deleteMonthlyGoal(goalId);
        }catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("delete Successful", HttpStatus.OK);
    }
}
