package com.mcfuturepartners.crm.api.goal.service;

import com.mcfuturepartners.crm.api.goal.entity.Goal;
import com.mcfuturepartners.crm.api.revenue.entity.Revenue;

import java.util.List;

public interface GoalService {
    List<Goal> findAllGoalsInYear(Integer year);
    Goal setMonthlyGoal(Goal goal);
    Goal updateMonthlyGoal(Long goalId, Goal goal);
    void deleteMonthlyGoal(Long goalId);
    List<Goal> getCompanyLatestGoalByMonth(Integer month);

}
