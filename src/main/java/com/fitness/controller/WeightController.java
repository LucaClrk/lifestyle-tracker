package com.fitness.controller;

import com.fitness.dto.WeightLogRequest;
import com.fitness.model.WeightLog;
import com.fitness.service.WeightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/users/{userId}/weight")
@Tag(name = "Weight", description = "Track weight progress")
public class WeightController {

    private final WeightService weightService;

    public WeightController(WeightService weightService) {
        this.weightService = weightService;
    }

    @GetMapping
    @Operation(summary = "Get full weight history")
    public List<WeightLog> getHistory(@PathVariable Long userId) {
        return weightService.getHistory(userId);
    }

    @GetMapping("/latest")
    @Operation(summary = "Get latest weight entry")
    public WeightLog getLatest(@PathVariable Long userId) {
        return weightService.getLatest(userId)
                .orElseThrow(() -> new NoSuchElementException("No weight entries yet"));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Log a weight measurement")
    public WeightLog log(@PathVariable Long userId, @Valid @RequestBody WeightLogRequest req) {
        return weightService.log(userId, req);
    }

    @DeleteMapping("/{entryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a weight entry")
    public void delete(@PathVariable Long userId, @PathVariable Long entryId) {
        weightService.delete(entryId);
    }
}
