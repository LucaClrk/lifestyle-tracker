package com.fitness.dto;

public record GarminActivityDto(
        Long activityId,
        String activityName,
        String activityType,
        String startTimeLocal,
        Integer durationMinutes,
        Double distanceKm,
        Integer averageHR,
        Integer maxHR,
        Integer calories,
        Double elevationGain
) {}
