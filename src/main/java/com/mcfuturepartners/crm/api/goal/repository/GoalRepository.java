package com.mcfuturepartners.crm.api.goal.repository;

import com.mcfuturepartners.crm.api.goal.entity.Goal;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GoalRepository extends JpaRepository<Goal, Long> {
    Boolean existsByYearAndMonth(Integer year, Integer month);
    List<Goal> findGoalByYear(Integer year, Sort sort);
    Optional<Goal> findGoalByYearAndMonth(Integer year, Integer month);
}
