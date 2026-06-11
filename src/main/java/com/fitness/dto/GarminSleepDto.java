package com.fitness.dto;

public record GarminSleepDto(
        Integer totalSleepMinutes,
        Integer deepSleepMinutes,
        Integer lightSleepMinutes,
        Integer remSleepMinutes,
        Integer awakeMinutes
) {}
