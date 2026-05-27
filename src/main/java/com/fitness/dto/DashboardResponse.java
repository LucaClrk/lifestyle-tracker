package com.fitness.dto;

import java.util.List;

public class DashboardResponse {

    private String userName;
    private double currentWeightKg;
    private double targetWeightKg;
    private double weightToLoseKg;
    private double bmi;
    private int caloriesTodayTotal;
    private int habitsCompletedToday;
    private int habitsTotalToday;
    private List<UpcomingTraining> upcomingTrainings;
    private List<WeeklyProgress> weeklyHabitProgress;

    public DashboardResponse() {}

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public double getCurrentWeightKg() { return currentWeightKg; }
    public void setCurrentWeightKg(double currentWeightKg) { this.currentWeightKg = currentWeightKg; }
    public double getTargetWeightKg() { return targetWeightKg; }
    public void setTargetWeightKg(double targetWeightKg) { this.targetWeightKg = targetWeightKg; }
    public double getWeightToLoseKg() { return weightToLoseKg; }
    public void setWeightToLoseKg(double weightToLoseKg) { this.weightToLoseKg = weightToLoseKg; }
    public double getBmi() { return bmi; }
    public void setBmi(double bmi) { this.bmi = bmi; }
    public int getCaloriesTodayTotal() { return caloriesTodayTotal; }
    public void setCaloriesTodayTotal(int caloriesTodayTotal) { this.caloriesTodayTotal = caloriesTodayTotal; }
    public int getHabitsCompletedToday() { return habitsCompletedToday; }
    public void setHabitsCompletedToday(int habitsCompletedToday) { this.habitsCompletedToday = habitsCompletedToday; }
    public int getHabitsTotalToday() { return habitsTotalToday; }
    public void setHabitsTotalToday(int habitsTotalToday) { this.habitsTotalToday = habitsTotalToday; }
    public List<UpcomingTraining> getUpcomingTrainings() { return upcomingTrainings; }
    public void setUpcomingTrainings(List<UpcomingTraining> upcomingTrainings) { this.upcomingTrainings = upcomingTrainings; }
    public List<WeeklyProgress> getWeeklyHabitProgress() { return weeklyHabitProgress; }
    public void setWeeklyHabitProgress(List<WeeklyProgress> weeklyHabitProgress) { this.weeklyHabitProgress = weeklyHabitProgress; }

    public static class UpcomingTraining {
        private Long id;
        private String sportType;
        private String scheduledAt;
        private int plannedDurationMinutes;
        private Double distanceKm;

        public UpcomingTraining() {}

        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getSportType() { return sportType; }
        public void setSportType(String sportType) { this.sportType = sportType; }
        public String getScheduledAt() { return scheduledAt; }
        public void setScheduledAt(String scheduledAt) { this.scheduledAt = scheduledAt; }
        public int getPlannedDurationMinutes() { return plannedDurationMinutes; }
        public void setPlannedDurationMinutes(int plannedDurationMinutes) { this.plannedDurationMinutes = plannedDurationMinutes; }
        public Double getDistanceKm() { return distanceKm; }
        public void setDistanceKm(Double distanceKm) { this.distanceKm = distanceKm; }
    }

    public static class WeeklyProgress {
        private String habitName;
        private int completedDays;
        private int targetDays;
        private double completionRate;

        public WeeklyProgress() {}

        public String getHabitName() { return habitName; }
        public void setHabitName(String habitName) { this.habitName = habitName; }
        public int getCompletedDays() { return completedDays; }
        public void setCompletedDays(int completedDays) { this.completedDays = completedDays; }
        public int getTargetDays() { return targetDays; }
        public void setTargetDays(int targetDays) { this.targetDays = targetDays; }
        public double getCompletionRate() { return completionRate; }
        public void setCompletionRate(double completionRate) { this.completionRate = completionRate; }
    }
}
