package com.fitness.service;

import com.fitness.dto.TrainingCompleteRequest;
import com.fitness.dto.TrainingRequest;
import com.fitness.model.SportType;
import com.fitness.model.Training;
import com.fitness.repository.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final UserService userService;

    public TrainingService(TrainingRepository trainingRepository, UserService userService) {
        this.trainingRepository = trainingRepository;
        this.userService = userService;
    }

    public List<Training> getUpcoming(Long userId) {
        return trainingRepository.findByUserIdAndCompletedFalseAndScheduledAtAfterOrderByScheduledAtAsc(
                userId, LocalDateTime.now().minusHours(1));
    }

    public List<Training> getAll(Long userId) {
        return trainingRepository.findByUserIdOrderByScheduledAtAsc(userId);
    }

    public List<Training> getBySport(Long userId, SportType sport) {
        return trainingRepository.findByUserIdAndSportType(userId, sport);
    }

    public List<Training> getForWeek(Long userId) {
        LocalDateTime monday = LocalDateTime.now().with(DayOfWeek.MONDAY)
                .toLocalDate().atStartOfDay();
        return trainingRepository.findByUserIdAndScheduledAtBetweenOrderByScheduledAtAsc(
                userId, monday, monday.plusDays(7));
    }

    @Transactional
    public Training schedule(Long userId, TrainingRequest req) {
        Training training = new Training();
        training.setUser(userService.getById(userId));
        training.setSportType(req.sportType());
        training.setScheduledAt(req.scheduledAt());
        training.setPlannedDurationMinutes(req.plannedDurationMinutes());
        training.setDistanceKm(req.distanceKm());
        training.setNotes(req.notes());
        return trainingRepository.save(training);
    }

    @Transactional
    public Training update(Long id, TrainingRequest req) {
        Training training = getById(id);
        training.setSportType(req.sportType());
        training.setScheduledAt(req.scheduledAt());
        training.setPlannedDurationMinutes(req.plannedDurationMinutes());
        training.setDistanceKm(req.distanceKm());
        training.setNotes(req.notes());
        return trainingRepository.save(training);
    }

    @Transactional
    public Training complete(Long id, TrainingCompleteRequest req) {
        Training training = getById(id);
        training.setCompleted(true);
        training.setActualDurationMinutes(req.actualDurationMinutes());
        training.setFeedback(req.feedback());
        return trainingRepository.save(training);
    }

    @Transactional
    public void delete(Long id) {
        trainingRepository.deleteById(id);
    }

    public Training getById(Long id) {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Training not found: " + id));
    }
}
