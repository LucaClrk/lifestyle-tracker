package com.fitness.dto;

import com.fitness.model.SportType;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

public record TrainingRequest(
        @NotNull SportType sportType,
        @NotNull LocalDateTime scheduledAt,
        @Min(1) int plannedDurationMinutes,
        Double distanceKm,
        String notes
) {}
