package com.fitness.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "habits")
public class Habit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User user;

    @NotBlank
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private HabitCategory category;

    @Min(1) @Max(7)
    private int targetDaysPerWeek;

    private boolean active = true;

    public Habit() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public HabitCategory getCategory() { return category; }
    public void setCategory(HabitCategory category) { this.category = category; }
    public int getTargetDaysPerWeek() { return targetDaysPerWeek; }
    public void setTargetDaysPerWeek(int targetDaysPerWeek) { this.targetDaysPerWeek = targetDaysPerWeek; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
