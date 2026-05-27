package com.fitness.repository;

import com.fitness.model.Habit;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HabitRepository extends JpaRepository<Habit, Long> {
    List<Habit> findByUserIdAndActiveTrue(Long userId);
    List<Habit> findByUserId(Long userId);
}
