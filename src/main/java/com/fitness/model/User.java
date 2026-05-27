package com.fitness.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @Email
    @Column(unique = true)
    private String email;

    @Min(1) @Max(120)
    private int age;

    private double weightKg;
    private double heightCm;
    private double targetWeightKg;
    private LocalDate createdAt;

    public User() {}

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
    }

    public double getBmi() {
        double h = heightCm / 100.0;
        return Math.round((weightKg / (h * h)) * 10.0) / 10.0;
    }

    public double getWeightToLose() {
        return Math.max(0, weightKg - targetWeightKg);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public double getWeightKg() { return weightKg; }
    public void setWeightKg(double weightKg) { this.weightKg = weightKg; }
    public double getHeightCm() { return heightCm; }
    public void setHeightCm(double heightCm) { this.heightCm = heightCm; }
    public double getTargetWeightKg() { return targetWeightKg; }
    public void setTargetWeightKg(double targetWeightKg) { this.targetWeightKg = targetWeightKg; }
    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }
}
