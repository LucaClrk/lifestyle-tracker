package com.fitness.repository;

import com.fitness.model.FoodEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.time.LocalDate;
import java.util.List;

public interface FoodEntryRepository extends JpaRepository<FoodEntry, Long> {
    List<FoodEntry> findByUserIdAndEntryDateOrderByMealTypeAsc(Long userId, LocalDate date);
    List<FoodEntry> findByUserIdAndEntryDateBetween(Long userId, LocalDate from, LocalDate to);

    @Query("SELECT COALESCE(SUM(f.calories), 0) FROM FoodEntry f WHERE f.user.id = :userId AND f.entryDate = :date")
    int sumCaloriesByUserIdAndDate(Long userId, LocalDate date);
}
