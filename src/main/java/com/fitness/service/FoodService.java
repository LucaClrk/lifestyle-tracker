package com.fitness.service;

import com.fitness.dto.FoodEntryRequest;
import com.fitness.model.FoodEntry;
import com.fitness.repository.FoodEntryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class FoodService {

    private final FoodEntryRepository foodEntryRepository;
    private final UserService userService;

    public FoodService(FoodEntryRepository foodEntryRepository, UserService userService) {
        this.foodEntryRepository = foodEntryRepository;
        this.userService = userService;
    }

    public List<FoodEntry> getForDay(Long userId, LocalDate date) {
        return foodEntryRepository.findByUserIdAndEntryDateOrderByMealTypeAsc(userId, date);
    }

    public List<FoodEntry> getForToday(Long userId) {
        return getForDay(userId, LocalDate.now());
    }

    public int getCaloriesToday(Long userId) {
        return foodEntryRepository.sumCaloriesByUserIdAndDate(userId, LocalDate.now());
    }

    @Transactional
    public FoodEntry add(Long userId, FoodEntryRequest req) {
        FoodEntry entry = new FoodEntry();
        entry.setUser(userService.getById(userId));
        entry.setEntryDate(req.entryDate());
        entry.setMealType(req.mealType());
        entry.setFoodName(req.foodName());
        entry.setCalories(req.calories());
        entry.setProteinG(req.proteinG());
        entry.setCarbsG(req.carbsG());
        entry.setFatG(req.fatG());
        entry.setQuantity(req.quantity());
        entry.setUnit(req.unit());
        return foodEntryRepository.save(entry);
    }

    @Transactional
    public FoodEntry update(Long id, FoodEntryRequest req) {
        FoodEntry entry = getById(id);
        entry.setMealType(req.mealType());
        entry.setFoodName(req.foodName());
        entry.setCalories(req.calories());
        entry.setProteinG(req.proteinG());
        entry.setCarbsG(req.carbsG());
        entry.setFatG(req.fatG());
        entry.setQuantity(req.quantity());
        entry.setUnit(req.unit());
        return foodEntryRepository.save(entry);
    }

    @Transactional
    public void delete(Long id) {
        foodEntryRepository.deleteById(id);
    }

    public FoodEntry getById(Long id) {
        return foodEntryRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Food entry not found: " + id));
    }
}
