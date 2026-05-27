package com.fitness.dto;

import com.fitness.model.MealType;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record FoodEntryRequest(
        @NotNull LocalDate entryDate,
        @NotNull MealType mealType,
        @NotBlank String foodName,
        @Min(0) int calories,
        @DecimalMin("0.0") double proteinG,
        @DecimalMin("0.0") double carbsG,
        @DecimalMin("0.0") double fatG,
        @DecimalMin("0.0") double quantity,
        String unit
) {}
