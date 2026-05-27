package com.fitness.repository;

import com.fitness.model.HabitLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface HabitLogRepository extends JpaRepository<HabitLog, Long> {
    List<HabitLog> findByHabitId(Long habitId);
    List<HabitLog> findByHabitIdAndDateBetween(Long habitId, LocalDate from, LocalDate to);
    Optional<HabitLog> findByHabitIdAndDate(Long habitId, LocalDate date);

    @Query("SELECT h FROM HabitLog h WHERE h.habit.user.id = :userId AND h.date = :date")
    List<HabitLog> findByUserIdAndDate(Long userId, LocalDate date);

    @Query("SELECT h FROM HabitLog h WHERE h.habit.user.id = :userId AND h.date BETWEEN :from AND :to")
    List<HabitLog> findByUserIdAndDateBetween(Long userId, LocalDate from, LocalDate to);
}
