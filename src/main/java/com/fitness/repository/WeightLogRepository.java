package com.fitness.repository;

import com.fitness.model.WeightLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface WeightLogRepository extends JpaRepository<WeightLog, Long> {
    List<WeightLog> findByUserIdOrderByDateAsc(Long userId);
    Optional<WeightLog> findTopByUserIdOrderByDateDesc(Long userId);
    Optional<WeightLog> findByUserIdAndDate(Long userId, LocalDate date);
    List<WeightLog> findByUserIdAndDateBetweenOrderByDateAsc(Long userId, LocalDate from, LocalDate to);
}
