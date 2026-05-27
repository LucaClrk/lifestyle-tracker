package com.fitness.service;

import com.fitness.dto.WeightLogRequest;
import com.fitness.model.WeightLog;
import com.fitness.repository.WeightLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class WeightService {

    private final WeightLogRepository weightLogRepository;
    private final UserService userService;

    public WeightService(WeightLogRepository weightLogRepository, UserService userService) {
        this.weightLogRepository = weightLogRepository;
        this.userService = userService;
    }

    public List<WeightLog> getHistory(Long userId) {
        return weightLogRepository.findByUserIdOrderByDateAsc(userId);
    }

    public Optional<WeightLog> getLatest(Long userId) {
        return weightLogRepository.findTopByUserIdOrderByDateDesc(userId);
    }

    @Transactional
    public WeightLog log(Long userId, WeightLogRequest req) {
        WeightLog entry = weightLogRepository.findByUserIdAndDate(userId, req.date())
                .orElse(new WeightLog());
        entry.setUser(userService.getById(userId));
        entry.setDate(req.date());
        entry.setWeightKg(req.weightKg());
        entry.setNotes(req.notes());
        return weightLogRepository.save(entry);
    }

    @Transactional
    public void delete(Long id) {
        weightLogRepository.deleteById(id);
    }

    public WeightLog getById(Long id) {
        return weightLogRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Weight log not found: " + id));
    }
}
