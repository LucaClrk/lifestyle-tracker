package com.fitness.config;

import com.fitness.model.*;
import com.fitness.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);

    private final UserRepository userRepository;
    private final HabitRepository habitRepository;
    private final TrainingRepository trainingRepository;
    private final WeightLogRepository weightLogRepository;

    public DataInitializer(UserRepository userRepository, HabitRepository habitRepository,
                           TrainingRepository trainingRepository, WeightLogRepository weightLogRepository) {
        this.userRepository = userRepository;
        this.habitRepository = habitRepository;
        this.trainingRepository = trainingRepository;
        this.weightLogRepository = weightLogRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() > 0) return;

        log.info("Seeding initial data...");

        User luca = new User();
        luca.setName("Luca");
        luca.setEmail("luca@robaws.be");
        luca.setAge(21);
        luca.setWeightKg(90.0);
        luca.setHeightCm(196.0);
        luca.setTargetWeightKg(85.0);
        luca = userRepository.save(luca);

        WeightLog startWeight = new WeightLog();
        startWeight.setUser(luca);
        startWeight.setDate(LocalDate.now());
        startWeight.setWeightKg(90.0);
        startWeight.setNotes("Starting weight");
        weightLogRepository.save(startWeight);

        saveHabit(luca, "Drink 3L water", "Stay hydrated", HabitCategory.HYDRATION, 7);
        saveHabit(luca, "Sleep 8 hours", "Quality rest for recovery", HabitCategory.SLEEP, 7);
        saveHabit(luca, "Eat enough protein", "Target 150g protein/day", HabitCategory.NUTRITION, 7);
        saveHabit(luca, "Stretch / mobility", "10 min post-workout stretch", HabitCategory.RECOVERY, 5);

        LocalDateTime base = LocalDateTime.now().plusDays(1).withHour(18).withMinute(0).withSecond(0).withNano(0);
        saveTraining(luca, SportType.RUNNING, base, 45, 7.0, "Easy pace, zone 2");
        saveTraining(luca, SportType.TENNIS, base.plusDays(1), 90, null, "Practice serve + rallies");
        saveTraining(luca, SportType.CYCLING, base.plusDays(3), 60, 25.0, "Weekend endurance ride");

        log.info("Seed complete — user ID: {}, BMI: {}", luca.getId(), luca.getBmi());
    }

    private void saveHabit(User user, String name, String desc, HabitCategory cat, int days) {
        Habit h = new Habit();
        h.setUser(user);
        h.setName(name);
        h.setDescription(desc);
        h.setCategory(cat);
        h.setTargetDaysPerWeek(days);
        habitRepository.save(h);
    }

    private void saveTraining(User user, SportType type, LocalDateTime at, int mins, Double km, String notes) {
        Training t = new Training();
        t.setUser(user);
        t.setSportType(type);
        t.setScheduledAt(at);
        t.setPlannedDurationMinutes(mins);
        t.setDistanceKm(km);
        t.setNotes(notes);
        trainingRepository.save(t);
    }
}
