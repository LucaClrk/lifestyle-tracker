package com.fitness.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record WeightLogRequest(
        @NotNull LocalDate date,
        @DecimalMin("30.0") @DecimalMax("300.0") double weightKg,
        String notes
) {}
