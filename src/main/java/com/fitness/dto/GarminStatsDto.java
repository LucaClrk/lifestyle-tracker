package com.fitness.dto;

public record GarminStatsDto(
        Integer totalSteps,
        Integer dailyStepGoal,
        Integer totalKilocalories,
        Integer activeKilocalories,
        Integer restingHeartRate,
        Integer averageStressLevel,
        Integer floorsAscended
) {}
