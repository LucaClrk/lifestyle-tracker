package com.fitness.dto;

import com.fitness.model.HabitCategory;
import jakarta.validation.constraints.*;

public record HabitRequest(
        @NotBlank String name,
        String description,
        HabitCategory category,
        @Min(1) @Max(7) int targetDaysPerWeek
) {}
