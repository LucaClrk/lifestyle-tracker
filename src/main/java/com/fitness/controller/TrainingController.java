package com.fitness.controller;

import com.fitness.dto.GarminSyncResult;
import com.fitness.dto.TrainingCompleteRequest;
import com.fitness.dto.TrainingRequest;
import com.fitness.model.SportType;
import com.fitness.model.Training;
import com.fitness.service.TrainingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/trainings")
@Tag(name = "Trainings", description = "Schedule and track workouts")
public class TrainingController {

    private final TrainingService trainingService;

    public TrainingController(TrainingService trainingService) {
        this.trainingService = trainingService;
    }

    @GetMapping
    @Operation(summary = "Get all trainings")
    public List<Training> getAll(@PathVariable Long userId) {
        return trainingService.getAll(userId);
    }

    @GetMapping("/upcoming")
    @Operation(summary = "Get upcoming trainings")
    public List<Training> getUpcoming(@PathVariable Long userId) {
        return trainingService.getUpcoming(userId);
    }

    @GetMapping("/week")
    @Operation(summary = "Get this week's trainings")
    public List<Training> getWeek(@PathVariable Long userId) {
        return trainingService.getForWeek(userId);
    }

    @GetMapping("/sport/{sport}")
    @Operation(summary = "Get trainings by sport type")
    public List<Training> getBySport(@PathVariable Long userId, @PathVariable SportType sport) {
        return trainingService.getBySport(userId, sport);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Schedule a training session")
    public Training schedule(@PathVariable Long userId, @Valid @RequestBody TrainingRequest req) {
        return trainingService.schedule(userId, req);
    }

    @PutMapping("/{trainingId}")
    @Operation(summary = "Update a training session")
    public Training update(@PathVariable Long userId, @PathVariable Long trainingId,
                           @Valid @RequestBody TrainingRequest req) {
        return trainingService.update(trainingId, req);
    }

    @PostMapping("/{trainingId}/complete")
    @Operation(summary = "Mark training as completed")
    public Training complete(@PathVariable Long userId, @PathVariable Long trainingId,
                             @RequestBody TrainingCompleteRequest req) {
        return trainingService.complete(trainingId, req);
    }

    @PostMapping("/{trainingId}/uncomplete")
    @Operation(summary = "Undo completion of a training")
    public Training uncomplete(@PathVariable Long userId, @PathVariable Long trainingId) {
        return trainingService.uncomplete(trainingId);
    }

    @DeleteMapping("/{trainingId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a training session")
    public void delete(@PathVariable Long userId, @PathVariable Long trainingId) {
        trainingService.delete(trainingId);
    }

    @PostMapping("/sync-garmin")
    @Operation(summary = "Auto-complete trainings matched to recent Garmin activities")
    public GarminSyncResult syncGarmin(@PathVariable Long userId) {
        return trainingService.syncWithGarmin(userId);
    }
}
