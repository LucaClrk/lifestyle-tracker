package com.fitness.controller;

import com.fitness.dto.HabitLogRequest;
import com.fitness.dto.HabitRequest;
import com.fitness.model.Habit;
import com.fitness.model.HabitLog;
import com.fitness.service.HabitService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/habits")
@Tag(name = "Habits", description = "Track daily habits")
public class HabitController {

    private final HabitService habitService;

    public HabitController(HabitService habitService) {
        this.habitService = habitService;
    }

    @GetMapping
    @Operation(summary = "Get active habits for user")
    public List<Habit> getActive(@PathVariable Long userId) {
        return habitService.getActiveHabits(userId);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all habits including inactive")
    public List<Habit> getAll(@PathVariable Long userId) {
        return habitService.getAllHabits(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new habit")
    public Habit create(@PathVariable Long userId, @Valid @RequestBody HabitRequest req) {
        return habitService.create(userId, req);
    }

    @PutMapping("/{habitId}")
    @Operation(summary = "Update a habit")
    public Habit update(@PathVariable Long userId, @PathVariable Long habitId,
                        @Valid @RequestBody HabitRequest req) {
        return habitService.update(habitId, req);
    }

    @DeleteMapping("/{habitId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Deactivate a habit")
    public void deactivate(@PathVariable Long userId, @PathVariable Long habitId) {
        habitService.deactivate(habitId);
    }

    @PostMapping("/{habitId}/log")
    @Operation(summary = "Log habit completion for a day")
    public HabitLog log(@PathVariable Long userId, @PathVariable Long habitId,
                        @Valid @RequestBody HabitLogRequest req) {
        return habitService.logHabit(habitId, req);
    }

    @GetMapping("/logs/today")
    @Operation(summary = "Get today's habit logs")
    public List<HabitLog> getLogsToday(@PathVariable Long userId) {
        return habitService.getLogsForToday(userId);
    }

    @GetMapping("/logs/week")
    @Operation(summary = "Get this week's habit logs")
    public List<HabitLog> getLogsWeek(@PathVariable Long userId) {
        return habitService.getLogsForWeek(userId);
    }
}
