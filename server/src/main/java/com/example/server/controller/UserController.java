package com.example.server.controller;

import com.example.server.dto.UserLoginDto;
import com.example.server.dto.UserRegistrationDto;
import com.example.server.dto.UserUpdateDto;
import com.example.server.model.User;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    // http://localhost:8080/users/1
    //                            ^ user id here
    @PutMapping("/users/{userId}")
    public User updateUser(@PathVariable Integer userId, @RequestBody UserUpdateDto updateDto) {
        return userService.updateUser(userId, updateDto);
    }
}