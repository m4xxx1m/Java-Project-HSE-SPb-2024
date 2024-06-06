package com.example.server.service;

import com.example.server.dto.UserLoginDto;
import com.example.server.dto.UserRegistrationDto;
import com.example.server.dto.UserUpdateDto;
import com.example.server.model.Role;
import com.example.server.model.User;
import com.example.server.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public User registerUser(UserRegistrationDto registrationDto) {
//        User existingUser = userRepository.findByUsername(registrationDto.getUsername());
//        if (existingUser != null) {
//            throw new RuntimeException("Username already in use");
//        }
//
//        User user = new User();
//        user.setUsername(registrationDto.getUsername());
//        user.setEmail(registrationDto.getEmail());
//        user.setPassword(hashPassword(registrationDto.getPassword()));
//
//        return userRepository.save(user);
//    }

//    public User loginUser(UserLoginDto loginDto) {
//        User user = userRepository.findByUsername(loginDto.getUsername());
//        if (user != null && user.getPassword().equals(hashPassword(loginDto.getPassword()))) {
//            return user;
//        }
//        throw new RuntimeException("Invalid username or password");
//    }

//    private String hashPassword(String password) {
//        try {
//            MessageDigest md = MessageDigest.getInstance("SHA-256");
//            byte[] hash = md.digest(password.getBytes(StandardCharsets.UTF_8));
//            BigInteger number = new BigInteger(1, hash);
//            StringBuilder hexString = new StringBuilder(number.toString(16));
//            while (hexString.length() < 32) {
//                hexString.insert(0, '0');
//            }
//            return hexString.toString();
//        } catch (NoSuchAlgorithmException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public User updateUser(Integer userId, UserUpdateDto updateDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setUsername(updateDto.getUsername());
        user.setEmail(updateDto.getEmail());
        user.setPassword(updateDto.getPassword());
        user.setProfilePictureUrl(updateDto.getProfilePictureUrl());
        user.setContacts(updateDto.getContacts());
        user.setBio(updateDto.getBio());
        user.setResumeUrl(updateDto.getResumeUrl());
        user.setTags(updateDto.getTags());

        return userRepository.save(user);
    }

    public void updateUserContacts(Integer userId, String contacts) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setContacts(contacts);

        userRepository.save(user);
    }

    public void updateUserBio(Integer userId, String bio) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setBio(bio);

        userRepository.save(user);
    }

    public void updateUserTags(Integer userId, String tags) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setTags(tags);

        userRepository.save(user);
    }

    public User getUser(Integer userId) {
        return userRepository.findById(userId).orElse(null);
    }

    public List<User> getUsersList(Set<Integer> userIds) {
        return userRepository.findAllById(userIds);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public User create(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already used");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already used");
        }

        return save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public void updateUsername(Integer userId, String username) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(username);
        userRepository.save(user);
    }

    public void updateEmail(Integer userId, String email) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEmail(email);
        userRepository.save(user);
    }

    public void updatePassword(Integer userId, String password) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(password);
        userRepository.save(user);
    }

    public void updateProfilePictureUrl(Integer userId, String profilePictureUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setProfilePictureUrl(profilePictureUrl);
        userRepository.save(user);
    }

    public void updateResumeUrl(Integer userId, String resumeUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setResumeUrl(resumeUrl);
        userRepository.save(user);
    }

    public void updateRole(Integer userId, Role role) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        userRepository.save(user);
    }
}
