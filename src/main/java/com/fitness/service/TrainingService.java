package com.fitness.service;

import com.fitness.dto.GarminActivityDto;
import com.fitness.dto.GarminSyncResult;
import com.fitness.dto.TrainingCompleteRequest;
import com.fitness.dto.TrainingRequest;
import com.fitness.model.SportType;
import com.fitness.model.Training;
import com.fitness.repository.TrainingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TrainingService {

    private final TrainingRepository trainingRepository;
    private final UserService userService;
    private final GarminService garminService;

    public TrainingService(TrainingRepository trainingRepository, UserService userService, GarminService garminService) {
        this.trainingRepository = trainingRepository;
        this.userService = userService;
        this.garminService = garminService;
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

    @Transactional
    public Training uncomplete(Long id) {
        Training training = getById(id);
        training.setCompleted(false);
        training.setActualDurationMinutes(null);
        training.setFeedback(null);
        training.setGarminActivityId(null);
        return trainingRepository.save(training);
    }

    @Transactional
    public GarminSyncResult syncWithGarmin(Long userId) {
        List<GarminActivityDto> activities = garminService.getActivities(30);
        int synced = 0;
        for (GarminActivityDto activity : activities) {
            SportType sport = mapGarminSportType(activity.activityType());
            LocalDate activityDate = parseActivityDate(activity.startTimeLocal());
            if (activityDate == null) continue;

            LocalDateTime dayStart = activityDate.atStartOfDay();
            LocalDateTime dayEnd = activityDate.plusDays(1).atStartOfDay();

            List<Training> candidates = trainingRepository
                    .findByUserIdAndCompletedFalseAndSportTypeAndScheduledAtBetween(userId, sport, dayStart, dayEnd);

            for (Training t : candidates) {
                if (t.getGarminActivityId() != null) continue;
                t.setCompleted(true);
                t.setActualDurationMinutes(activity.durationMinutes());
                t.setDistanceKm(activity.distanceKm());
                t.setGarminActivityId(activity.activityId());
                trainingRepository.save(t);
                synced++;
                break;
            }
        }
        return new GarminSyncResult(synced, activities.size() - synced);
    }

    private SportType mapGarminSportType(String activityType) {
        if (activityType == null) return SportType.OTHER;
        String lower = activityType.toLowerCase();
        if (lower.contains("running") || lower.contains("trail")) return SportType.RUNNING;
        if (lower.contains("cycling") || lower.contains("biking") || lower.contains("bike")) return SportType.CYCLING;
        if (lower.contains("tennis")) return SportType.TENNIS;
        return SportType.OTHER;
    }

    private LocalDate parseActivityDate(String startTimeLocal) {
        if (startTimeLocal == null || startTimeLocal.length() < 10) return null;
        try {
            return LocalDate.parse(startTimeLocal.substring(0, 10));
        } catch (Exception e) {
            return null;
        }
    }

    public Training getById(Long id) {
        return trainingRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Training not found: " + id));
    }
}
