package com.fitness.service;

import com.fitness.dto.UserRequest;
import com.fitness.model.User;
import com.fitness.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found: " + id));
    }

    @Transactional
    public User create(UserRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new IllegalArgumentException("Email already registered: " + req.email());
        }
        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());
        user.setAge(req.age());
        user.setWeightKg(req.weightKg());
        user.setHeightCm(req.heightCm());
        user.setTargetWeightKg(req.targetWeightKg());
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, UserRequest req) {
        User user = getById(id);
        user.setName(req.name());
        user.setAge(req.age());
        user.setWeightKg(req.weightKg());
        user.setHeightCm(req.heightCm());
        user.setTargetWeightKg(req.targetWeightKg());
        return userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
