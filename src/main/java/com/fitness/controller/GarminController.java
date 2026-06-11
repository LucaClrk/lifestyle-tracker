package com.fitness.controller;

import com.fitness.dto.GarminActivityDto;
import com.fitness.dto.GarminSleepDto;
import com.fitness.dto.GarminStatsDto;
import com.fitness.service.GarminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/garmin")
@Tag(name = "Garmin", description = "Garmin Connect integration")
public class GarminController {

    private final GarminService garminService;

    public GarminController(GarminService garminService) {
        this.garminService = garminService;
    }

    @GetMapping("/activities")
    @Operation(summary = "Get recent Garmin activities")
    public ResponseEntity<List<GarminActivityDto>> getActivities(
            @RequestParam(defaultValue = "10") int limit) {
        try {
            return ResponseEntity.ok(garminService.getActivities(limit));
        } catch (Exception e) {
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/stats")
    @Operation(summary = "Get today's Garmin daily stats")
    public ResponseEntity<GarminStatsDto> getStats() {
        try {
            return ResponseEntity.ok(garminService.getTodayStats());
        } catch (Exception e) {
            return ResponseEntity.status(503).build();
        }
    }

    @GetMapping("/sleep")
    @Operation(summary = "Get last night's sleep data")
    public ResponseEntity<GarminSleepDto> getSleep() {
        try {
            return ResponseEntity.ok(garminService.getSleepData());
        } catch (Exception e) {
            return ResponseEntity.status(503).build();
        }
    }
}
