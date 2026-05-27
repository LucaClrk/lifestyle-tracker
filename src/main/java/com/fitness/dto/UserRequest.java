package com.fitness.dto;

import jakarta.validation.constraints.*;

public record UserRequest(
        @NotBlank String name,
        @Email String email,
        @Min(1) @Max(120) int age,
        @DecimalMin("30.0") @DecimalMax("300.0") double weightKg,
        @DecimalMin("100.0") @DecimalMax("250.0") double heightCm,
        @DecimalMin("30.0") @DecimalMax("300.0") double targetWeightKg
) {}
