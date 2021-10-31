package com.mcfuturepartners.crm.api.goal.repository;

import com.mcfuturepartners.crm.api.goal.entity.Goal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goal, Long> {
}
