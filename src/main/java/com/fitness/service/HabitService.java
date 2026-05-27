package com.fitness.service;

import com.fitness.dto.HabitLogRequest;
import com.fitness.dto.HabitRequest;
import com.fitness.model.Habit;
import com.fitness.model.HabitLog;
import com.fitness.repository.HabitLogRepository;
import com.fitness.repository.HabitRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class HabitService {

    private final HabitRepository habitRepository;
    private final HabitLogRepository habitLogRepository;
    private final UserService userService;

    public HabitService(HabitRepository habitRepository, HabitLogRepository habitLogRepository, UserService userService) {
        this.habitRepository = habitRepository;
        this.habitLogRepository = habitLogRepository;
        this.userService = userService;
    }

    public List<Habit> getActiveHabits(Long userId) {
        return habitRepository.findByUserIdAndActiveTrue(userId);
    }

    public List<Habit> getAllHabits(Long userId) {
        return habitRepository.findByUserId(userId);
    }

    @Transactional
    public Habit create(Long userId, HabitRequest req) {
        Habit habit = new Habit();
        habit.setUser(userService.getById(userId));
        habit.setName(req.name());
        habit.setDescription(req.description());
        habit.setCategory(req.category());
        habit.setTargetDaysPerWeek(req.targetDaysPerWeek());
        return habitRepository.save(habit);
    }

    @Transactional
    public Habit update(Long id, HabitRequest req) {
        Habit habit = getById(id);
        habit.setName(req.name());
        habit.setDescription(req.description());
        habit.setCategory(req.category());
        habit.setTargetDaysPerWeek(req.targetDaysPerWeek());
        return habitRepository.save(habit);
    }

    @Transactional
    public void deactivate(Long id) {
        Habit habit = getById(id);
        habit.setActive(false);
        habitRepository.save(habit);
    }

    public Habit getById(Long id) {
        return habitRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Habit not found: " + id));
    }

    @Transactional
    public HabitLog logHabit(Long habitId, HabitLogRequest req) {
        Habit habit = getById(habitId);
        HabitLog log = habitLogRepository.findByHabitIdAndDate(habitId, req.date())
                .orElse(new HabitLog());
        log.setHabit(habit);
        log.setDate(req.date());
        log.setCompleted(req.completed());
        log.setNotes(req.notes());
        return habitLogRepository.save(log);
    }

    public List<HabitLog> getLogsForToday(Long userId) {
        return habitLogRepository.findByUserIdAndDate(userId, LocalDate.now());
    }

    public List<HabitLog> getLogsForWeek(Long userId) {
        LocalDate monday = LocalDate.now().with(DayOfWeek.MONDAY);
        return habitLogRepository.findByUserIdAndDateBetween(userId, monday, monday.plusDays(6));
    }
}
