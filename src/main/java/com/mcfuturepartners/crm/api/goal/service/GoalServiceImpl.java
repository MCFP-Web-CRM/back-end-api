package com.mcfuturepartners.crm.api.goal.service;

import com.mcfuturepartners.crm.api.goal.entity.Goal;
import com.mcfuturepartners.crm.api.goal.repository.GoalRepository;
import com.mcfuturepartners.crm.api.revenue.entity.Revenue;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
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
            return null;
        }
        return goalRepository.save(goal);
    }

    @Override
    public List<Goal> getCompanyLatestGoalByMonth(Integer month) {
        List<Goal> goalList = new ArrayList<>(month);

        LocalDateTime date = LocalDate.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1).atStartOfDay();

        for(int i = 0 ; i < month ; i ++){
            goalList.add(goalRepository.findGoalByYearAndMonth(
                    date.getYear(), date.getMonthValue())
                    .orElse(Goal.builder().year(date.getYear()).month(date.getMonthValue()).revenueAmount(0L).build()));

            date = date.minusMonths(1);
        }
        return goalList;
    }

}
