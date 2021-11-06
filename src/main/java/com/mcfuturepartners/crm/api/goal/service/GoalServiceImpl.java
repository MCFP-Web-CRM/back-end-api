package com.mcfuturepartners.crm.api.goal.service;

import com.mcfuturepartners.crm.api.exception.ErrorCode;
import com.mcfuturepartners.crm.api.exception.FindException;
import com.mcfuturepartners.crm.api.goal.entity.Goal;
import com.mcfuturepartners.crm.api.goal.repository.GoalRepository;
import com.mcfuturepartners.crm.api.revenue.entity.Revenue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService{
    private final GoalRepository goalRepository;

    @Override
    public Goal setMonthlyGoal(Goal goal) {
        if(goalRepository.existsByYearAndMonth(goal.getYear(), goal.getMonth())){
            throw new FindException("Already Exists");
        }

        try{
            goal=goalRepository.save(goal);
        }catch(Exception e){
            throw e;
        }
        return goal;
    }

    @Override
    public Goal updateMonthlyGoal(Long goalId, Goal goal) {
        Goal originalGoal = goalRepository.findById(goalId).orElseThrow(()-> new FindException("Goal "+ ErrorCode.RESOURCE_NOT_FOUND.getMsg()));
        if(!originalGoal.getYear().equals(goal.getYear())||!originalGoal.getMonth().equals(goal.getMonth())){
            return null;
        }
        originalGoal.updateModified(goal);

        try{
            originalGoal = goalRepository.save(originalGoal);
        }catch (Exception e){
            throw e;
        }

        return originalGoal;
    }

    @Override
    public void deleteMonthlyGoal(Long goalId) {
        try{
            goalRepository.deleteById(goalId);
        } catch(Exception e){
            throw e;
        }
    }

    @Override
    public List<Goal> getCompanyLatestGoalByMonth(Integer month) {
        List<Goal> goalList = new ArrayList<>(month);

        LocalDateTime date = LocalDate.of(ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getYear(), ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime().getMonth(), 1).atStartOfDay();

        for(int i = 0 ; i < month ; i ++){
            goalList.add(goalRepository.findGoalByYearAndMonth(
                    date.getYear(), date.getMonthValue())
                    .orElse(Goal.builder().year(date.getYear()).month(date.getMonthValue()).revenueAmount(0L).build()));

            date = date.minusMonths(1);
        }
        return goalList;
    }

}
