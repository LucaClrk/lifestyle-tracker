package com.fitness.service;

import com.fitness.dto.DashboardResponse;
import com.fitness.model.Habit;
import com.fitness.model.HabitLog;
import com.fitness.model.Training;
import com.fitness.model.User;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class DashboardService {

    private final UserService userService;
    private final HabitService habitService;
    private final TrainingService trainingService;
    private final FoodService foodService;
    private final WeightService weightService;

    public DashboardService(UserService userService, HabitService habitService,
                            TrainingService trainingService, FoodService foodService,
                            WeightService weightService) {
        this.userService = userService;
        this.habitService = habitService;
        this.trainingService = trainingService;
        this.foodService = foodService;
        this.weightService = weightService;
    }

    public DashboardResponse getDashboard(Long userId) {
        User user = userService.getById(userId);

        double currentWeight = weightService.getLatest(userId)
                .map(WeightLog -> WeightLog.getWeightKg())
                .orElse(user.getWeightKg());

        List<Habit> activeHabits = habitService.getActiveHabits(userId);
        List<HabitLog> todayLogs = habitService.getLogsForToday(userId);
        long completedToday = todayLogs.stream().filter(HabitLog::isCompleted).count();

        List<Training> upcoming = trainingService.getUpcoming(userId).stream().limit(5).toList();

        List<DashboardResponse.UpcomingTraining> upcomingDtos = upcoming.stream().map(t -> {
            DashboardResponse.UpcomingTraining ut = new DashboardResponse.UpcomingTraining();
            ut.setId(t.getId());
            ut.setSportType(t.getSportType().name());
            ut.setScheduledAt(t.getScheduledAt().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            ut.setPlannedDurationMinutes(t.getPlannedDurationMinutes());
            ut.setDistanceKm(t.getDistanceKm());
            return ut;
        }).toList();

        List<HabitLog> weekLogs = habitService.getLogsForWeek(userId);

        List<DashboardResponse.WeeklyProgress> weeklyProgress = activeHabits.stream().map(habit -> {
            long done = weekLogs.stream()
                    .filter(l -> l.getHabit().getId().equals(habit.getId()) && l.isCompleted())
                    .count();
            double rate = habit.getTargetDaysPerWeek() > 0
                    ? Math.min(100.0, (done * 100.0) / habit.getTargetDaysPerWeek())
                    : 0;
            DashboardResponse.WeeklyProgress wp = new DashboardResponse.WeeklyProgress();
            wp.setHabitName(habit.getName());
            wp.setCompletedDays((int) done);
            wp.setTargetDays(habit.getTargetDaysPerWeek());
            wp.setCompletionRate(Math.round(rate * 10.0) / 10.0);
            return wp;
        }).toList();

        DashboardResponse response = new DashboardResponse();
        response.setUserName(user.getName());
        response.setCurrentWeightKg(currentWeight);
        response.setTargetWeightKg(user.getTargetWeightKg());
        response.setWeightToLoseKg(Math.max(0, currentWeight - user.getTargetWeightKg()));
        response.setBmi(user.getBmi());
        response.setCaloriesTodayTotal(foodService.getCaloriesToday(userId));
        response.setHabitsCompletedToday((int) completedToday);
        response.setHabitsTotalToday(activeHabits.size());
        response.setUpcomingTrainings(upcomingDtos);
        response.setWeeklyHabitProgress(weeklyProgress);
        return response;
    }
}
