package com.fitness.dto;

import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record HabitLogRequest(
        @NotNull LocalDate date,
        boolean completed,
        String notes
) {}
