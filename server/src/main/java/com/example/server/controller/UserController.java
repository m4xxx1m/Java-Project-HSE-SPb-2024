package com.example.server.controller;

import com.example.server.dto.UserLoginDto;
import com.example.server.dto.UserRegistrationDto;
import com.example.server.dto.UserUpdateDto;
import com.example.server.model.Subscription;
import com.example.server.model.User;
import com.example.server.service.SubscriptionService;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
public class UserController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;

    @Autowired
    public UserController(UserService userService, SubscriptionService subscriptionService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
    }

    // example of usage:
    // curl -X POST -H "Content-Type: application/json" -d '{"username":"testuser", "email":"testuser@example.com", "password":"testpassword"}' http://localhost:8080/register
    @PostMapping("/register")
    public User registerUser(@RequestBody UserRegistrationDto registrationDto) {
        return userService.registerUser(registrationDto);
    }

    // example of usage:
    // curl -i -X POST -H "Content-Type: application/json" -d '{"username":"testuser", "password":"testpassword"}' http://localhost:8080/login
    @PostMapping("/login")
    public ResponseEntity<User> loginUser(@RequestBody UserLoginDto loginDto) {
        try {
            User user = userService.loginUser(loginDto);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    // example of usage:
    // curl -X PUT -H "Content-Type: application/json" \
    // -d '{
    //    "username": "newUsername",
    //    "email": "newEmail@example.com",
    //    "password": "newPassword",
    //    "profilePictureUrl": "newProfilePictureUrl",
    //    "firstName": "newFirstName",
    //    "secondName": "newSecondName",
    //    "dateOfBirth": "1990-01-01",
    //    "country": "newCountry",
    //    "city": "newCity",
    //    "education": "newEducation",
    //    "bio": "newBio",
    //    "resumeUrl": "newResumeUrl"
    // }' \
    // http://localhost:8080/users/update/1
    //                                    ^ user id here
    @PutMapping("/users/update/{userId}")
    public User updateUser(@PathVariable Integer userId, @RequestBody UserUpdateDto updateDto) {
        return userService.updateUser(userId, updateDto);
    }

    @PostMapping("/users/{subscriberId}/subscribe/{subscribeToId}")
    public Subscription subscribe(@PathVariable Integer subscriberId, @PathVariable Integer subscribeToId) {
        return subscriptionService.subscribe(subscriberId, subscribeToId);
    }

    @GetMapping("/users/getUser/{userId}")
    public User getUser(@PathVariable Integer userId) {
        return userService.getUser(userId);
    }

    @PostMapping("/users/getUsersList")
    public List<User> getUsersList(@RequestBody Set<Integer> userIds) {
        return userService.getUsersList(userIds);
    }
}
