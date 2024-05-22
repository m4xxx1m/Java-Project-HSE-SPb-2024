package com.example.server.controller;

import com.example.server.dto.UserLoginDto;
import com.example.server.dto.UserRegistrationDto;
import com.example.server.dto.UserUpdateDto;
import com.example.server.model.Post;
import com.example.server.model.Subscription;
import com.example.server.model.User;
import com.example.server.service.SubscriptionService;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
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
    public ResponseEntity<User> updateUser(@PathVariable Integer userId, @ModelAttribute UserUpdateDto updateDto) {
        try {
            return ResponseEntity.ok(userService.updateUser(userId, updateDto));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/users/{subscriberId}/subscribe/{subscribeToId}")
    public Subscription subscribe(
            @PathVariable Integer subscriberId,
            @PathVariable Integer subscribeToId
    ) {
        return subscriptionService.subscribe(subscriberId, subscribeToId);
    }

    @PostMapping("/users/{subscriberId}/unsubscribe/{subscribeToId}")
    public void unsubscribe(@PathVariable Integer subscriberId, @PathVariable Integer subscribeToId) {
        subscriptionService.unsubscribe(subscriberId, subscribeToId);
    }

    @GetMapping("/users/{subscriberId}/checkSubscription/{subscribeToId}")
    public boolean checkSubscription(
            @PathVariable Integer subscriberId,
            @PathVariable Integer subscribeToId
    ) {
        return subscriptionService.checkSubscription(subscriberId, subscribeToId);
    }

    @GetMapping("/users/{subscriberId}/getSubscriptions")
    public List<User> getSubscriptions(@PathVariable Integer subscriberId) {
        return subscriptionService.getSubscriptions(subscriberId);
    }

    @GetMapping("/users/getUser/{userId}")
    public User getUser(@PathVariable Integer userId) {
        return userService.getUser(userId);
    }

    @GetMapping("/users/picture/{userId}")
    public ResponseEntity<?> getUserProfilePicture(@PathVariable Integer userId) {
        User user = userService.getUser(userId);
        String profilePictureUrl = user.getProfilePictureUrl();
        try {
            if (profilePictureUrl == null) {
                return ResponseEntity.ok(null);
            }
            Path path = Paths.get(profilePictureUrl);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(Files.probeContentType(path)))
                    .body(userService.getUserProfilePicture(userId));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping("/users/picture/{userId}/upload")
    public ResponseEntity<User> uploadUserProfilePicture(
            @PathVariable Integer userId,
            @ModelAttribute("profilePicture") MultipartFile profilePicture
    ) {
        try {
            return ResponseEntity.ok(userService.uploadUserProfilePicture(userId, profilePicture));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/users/picture/{userId}/delete")
    public ResponseEntity<User> deleteUserProfilePicture(@PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(userService.deleteUserProfilePicture(userId));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    @PostMapping("/users/getUsersList")
    public List<User> getUsersList(@RequestBody Set<Integer> userIds) {
        return userService.getUsersList(userIds);
    }

    @PostMapping("/users/{userId}/updateContacts")
    public void updateContacts(@PathVariable Integer userId, @RequestParam("contacts") String contacts) {
        userService.updateUserContacts(userId, contacts);
    }

    @PostMapping("/users/{userId}/updateBio")
    public void updateBio(@PathVariable Integer userId, @RequestParam("bio") String bio) {
        userService.updateUserBio(userId, bio);
    }

    @PostMapping("/users/{userId}/updateTags")
    public void updateTags(@PathVariable Integer userId, @RequestParam("tags") String tags) {
        userService.updateUserTags(userId, tags);
    }

    @GetMapping("/users/{userId}/getPostsFromSubscriptions")
    public List<Post> getPostsFromSubscriptions(@PathVariable Integer userId) {
        return subscriptionService.getPostsFromSubscriptions(userId);
    }

    @GetMapping("/users/{userId}/getUserTags")
    public String getUserTags(@PathVariable Integer userId) {
        User user = userService.getUser(userId);
        return user == null ? null : user.getTags();
    }
}
