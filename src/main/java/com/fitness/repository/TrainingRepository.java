package com.fitness.repository;

import com.fitness.model.SportType;
import com.fitness.model.Training;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TrainingRepository extends JpaRepository<Training, Long> {
    List<Training> findByUserIdOrderByScheduledAtAsc(Long userId);
    List<Training> findByUserIdAndScheduledAtBetweenOrderByScheduledAtAsc(Long userId, LocalDateTime from, LocalDateTime to);
    List<Training> findByUserIdAndSportType(Long userId, SportType sportType);
    List<Training> findByUserIdAndCompletedFalseAndScheduledAtAfterOrderByScheduledAtAsc(Long userId, LocalDateTime after);
    List<Training> findByUserIdAndCompletedFalseAndSportTypeAndScheduledAtBetween(Long userId, SportType sportType, LocalDateTime from, LocalDateTime to);
}
