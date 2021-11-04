package com.mcfuturepartners.crm.api.goal.service;

import com.mcfuturepartners.crm.api.goal.entity.Goal;
import com.mcfuturepartners.crm.api.revenue.entity.Revenue;

import java.util.List;

public interface GoalService {
    Goal setMonthlyGoal(Goal goal);

    List<Goal> getCompanyLatestGoalByMonth(Integer month);

}
