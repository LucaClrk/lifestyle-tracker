package com.fitness.service;

import com.fitness.dto.GarminActivityDto;
import com.fitness.dto.GarminSleepDto;
import com.fitness.dto.GarminStatsDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class GarminService {

    @Value("${garmin.service.url:http://localhost:5000}")
    private String garminServiceUrl;

    private final RestTemplate restTemplate;

    public GarminService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SuppressWarnings("unchecked")
    public List<GarminActivityDto> getActivities(int limit) {
        List<Map<String, Object>> raw = restTemplate.getForObject(
                garminServiceUrl + "/garmin/activities?limit=" + limit, List.class);
        if (raw == null) return List.of();
        return raw.stream().map(this::mapActivity).toList();
    }

    @SuppressWarnings("unchecked")
    public GarminStatsDto getTodayStats() {
        Map<String, Object> raw = restTemplate.getForObject(
                garminServiceUrl + "/garmin/stats", Map.class);
        if (raw == null) return null;
        return new GarminStatsDto(
                num(raw.get("totalSteps")),
                num(raw.get("dailyStepGoal")),
                num(raw.get("totalKilocalories")),
                num(raw.get("activeKilocalories")),
                num(raw.get("restingHeartRate")),
                num(raw.get("averageStressLevel")),
                num(raw.get("floorsAscendedInMeters") != null ? raw.get("floorsAscendedInMeters") : raw.get("floorsAscended"))
        );
    }

    @SuppressWarnings("unchecked")
    public GarminSleepDto getSleepData() {
        Map<String, Object> raw = restTemplate.getForObject(
                garminServiceUrl + "/garmin/sleep", Map.class);
        if (raw == null) return null;
        Map<String, Object> dto = (Map<String, Object>) raw.get("dailySleepDTO");
        if (dto == null) return null;
        return new GarminSleepDto(
                seconds(dto.get("sleepTimeSeconds")),
                seconds(dto.get("deepSleepSeconds")),
                seconds(dto.get("lightSleepSeconds")),
                seconds(dto.get("remSleepSeconds")),
                seconds(dto.get("awakeSleepSeconds"))
        );
    }

    @SuppressWarnings("unchecked")
    private GarminActivityDto mapActivity(Map<String, Object> m) {
        String type = null;
        Object at = m.get("activityType");
        if (at instanceof Map<?, ?> atMap) {
            type = (String) atMap.get("typeKey");
        }
        Double distMeters = dbl(m.get("distance"));
        Double distKm = distMeters != null ? Math.round(distMeters / 10.0) / 100.0 : null;
        return new GarminActivityDto(
                m.get("activityId") instanceof Number n ? n.longValue() : null,
                (String) m.get("activityName"),
                type,
                (String) m.get("startTimeLocal"),
                m.get("duration") instanceof Number n ? (int) (n.doubleValue() / 60) : null,
                distKm,
                num(m.get("averageHR")),
                num(m.get("maxHR")),
                num(m.get("calories")),
                dbl(m.get("elevationGain"))
        );
    }

    private Integer num(Object v) {
        return v instanceof Number n ? n.intValue() : null;
    }

    private Double dbl(Object v) {
        return v instanceof Number n ? n.doubleValue() : null;
    }

    private Integer seconds(Object v) {
        return v instanceof Number n ? n.intValue() / 60 : null;
    }
}
