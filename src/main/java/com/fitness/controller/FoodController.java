package com.fitness.controller;

import com.fitness.dto.FoodEntryRequest;
import com.fitness.model.FoodEntry;
import com.fitness.service.FoodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/food")
@Tag(name = "Food", description = "Track meals and nutrition")
public class FoodController {

    private final FoodService foodService;

    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/today")
    @Operation(summary = "Get today's food entries")
    public List<FoodEntry> getToday(@PathVariable Long userId) {
        return foodService.getForToday(userId);
    }

    @GetMapping
    @Operation(summary = "Get food entries for a specific date")
    public List<FoodEntry> getForDay(@PathVariable Long userId,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return foodService.getForDay(userId, date);
    }

    @GetMapping("/today/calories")
    @Operation(summary = "Get total calories for today")
    public int getTodayCalories(@PathVariable Long userId) {
        return foodService.getCaloriesToday(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Add a food entry")
    public FoodEntry add(@PathVariable Long userId, @Valid @RequestBody FoodEntryRequest req) {
        return foodService.add(userId, req);
    }

    @PutMapping("/{entryId}")
    @Operation(summary = "Update a food entry")
    public FoodEntry update(@PathVariable Long userId, @PathVariable Long entryId,
                            @Valid @RequestBody FoodEntryRequest req) {
        return foodService.update(entryId, req);
    }

    @DeleteMapping("/{entryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a food entry")
    public void delete(@PathVariable Long userId, @PathVariable Long entryId) {
        foodService.delete(entryId);
    }
}
