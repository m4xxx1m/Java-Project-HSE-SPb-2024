package com.example.server.service;

import com.example.server.dto.UserLoginDto;
import com.example.server.dto.UserRegistrationDto;
import com.example.server.model.User;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User registerUser(UserRegistrationDto registrationDto) {
        User existingUser = userRepository.findByUsername(registrationDto.getUsername());
        if (existingUser != null) {
            throw new RuntimeException("Username already in use");
        }

        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(hashPassword(registrationDto.getPassword()));

        return userRepository.save(user);
    }

    public User loginUser(UserLoginDto loginDto) {
        User user = userRepository.findByUsername(loginDto.getUsername());
        if (user != null && user.getPassword().equals(hashPassword(loginDto.getPassword()))) {
            return user;
        }
        throw new RuntimeException("Invalid username or password");
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
            BigInteger number = new BigInteger(1, hash);
            StringBuilder hexString = new StringBuilder(number.toString(16));
            while (hexString.length() < 32) {
                hexString.insert(0, '0');
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
