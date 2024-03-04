package com.example.server.controller;

import com.example.server.dto.UserRegistrationDto;
import com.example.server.model.User;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
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

}