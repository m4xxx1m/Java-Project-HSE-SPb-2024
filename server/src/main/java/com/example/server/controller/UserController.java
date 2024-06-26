package com.example.server.controller;

import com.example.server.dto.ResumeDto;
import com.example.server.dto.UserUpdateDto;
import com.example.server.model.FileInfo;
import com.example.server.model.Post;
import com.example.server.model.Role;
import com.example.server.model.Subscription;
import com.example.server.model.User;
import com.example.server.service.FileInfoService;
import com.example.server.service.ResumeService;
import com.example.server.service.SubscriptionService;
import com.example.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@RestController
public class UserController {

    private final UserService userService;
    private final SubscriptionService subscriptionService;
    private final FileInfoService fileInfoService;

    private final ResumeService resumeService;

    @Autowired
    public UserController(UserService userService,
                          SubscriptionService subscriptionService,
                          FileInfoService fileInfoService,
                          ResumeService resumeService) {
        this.userService = userService;
        this.subscriptionService = subscriptionService;
        this.fileInfoService = fileInfoService;
        this.resumeService = resumeService;
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
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(userService.updateUser(userId, updateDto));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/users/{subscriberId}/subscribe/{subscribeToId}")
    public ResponseEntity<Subscription> subscribe(
            @PathVariable Integer subscriberId,
            @PathVariable Integer subscribeToId
    ) {
        if (!Objects.equals(AuthController.getCurrentUserId(), subscriberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(subscriptionService.subscribe(subscriberId, subscribeToId));
    }

    @PostMapping("/users/{subscriberId}/unsubscribe/{subscribeToId}")
    public ResponseEntity<Void> unsubscribe(@PathVariable Integer subscriberId, @PathVariable Integer subscribeToId) {
        if (!Objects.equals(AuthController.getCurrentUserId(), subscriberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        subscriptionService.unsubscribe(subscriberId, subscribeToId);
        return new ResponseEntity<>(HttpStatus.OK);
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
            if (profilePictureUrl == null || profilePictureUrl.isEmpty()) {
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
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(userService.uploadUserProfilePicture(userId, profilePicture));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/users/picture/{userId}/delete")
    public ResponseEntity<User> deleteUserProfilePicture(@PathVariable Integer userId) {
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
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
    public ResponseEntity<Void> updateContacts(@PathVariable Integer userId, @RequestParam("contacts") String contacts) {
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.updateUserContacts(userId, contacts);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/updateBio")
    public ResponseEntity<Void> updateBio(@PathVariable Integer userId, @RequestParam("bio") String bio) {
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.updateUserBio(userId, bio);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/updateTags")
    public ResponseEntity<Void> updateTags(@PathVariable Integer userId, @RequestParam("tags") String tags) {
        if (!Objects.equals(AuthController.getCurrentUserId(), userId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.updateUserTags(userId, tags);
        return ResponseEntity.ok().build();
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

    @PostMapping("/users/{userId}/updateUsername")
    public void updateUsername(@PathVariable Integer userId, @RequestParam("username") String username) {
        userService.updateUsername(userId, username);
    }

    @PostMapping("/users/{userId}/updateEmail")
    public void updateEmail(@PathVariable Integer userId, @RequestParam("email") String email) {
        userService.updateEmail(userId, email);
    }

    @PostMapping("/users/{userId}/updatePassword")
    public void updatePassword(@PathVariable Integer userId, @RequestParam("password") String password) {
        userService.updatePassword(userId, password);
    }

    @PostMapping("/users/{userId}/updateProfilePictureUrl")
    public void updateProfilePictureUrl(@PathVariable Integer userId, @RequestParam("profilePictureUrl") String profilePictureUrl) {
        userService.updateProfilePictureUrl(userId, profilePictureUrl);
    }

    @PostMapping("/users/{userId}/updateRole")
    public void updateRole(@PathVariable Integer userId, @RequestParam("role") Role role) {
        userService.updateRole(userId, role);
    }

    @GetMapping(value = "/user/{id}/resume")
    ResponseEntity<?> getResume(@PathVariable Integer id) {
        User user = userService.getUser(id);
        if (user.getResumeInfoId() == null) {
            return ResponseEntity.ok(null);
        }
        FileInfo resumeInfo = fileInfoService.findById(user.getResumeInfoId());
        try {
            byte[] fileData = userService.getResumeData(id + "//" + resumeInfo.getKey());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf(resumeInfo.getFileType()))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resumeInfo.getFileName() + "\"")
                    .body(fileData);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping(value = "/user/{id}/resume/add")
    ResponseEntity<User> addResume(@PathVariable Integer id,
                                   @ModelAttribute("resume") MultipartFile resume) {
        if (!Objects.equals(AuthController.getCurrentUserId(), id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(userService.uploadResume(id, resume));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping(value = "/user/{id}/resume/delete")
    ResponseEntity<User> deleteResume(@PathVariable Integer id) {
        if (!Objects.equals(AuthController.getCurrentUserId(), id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            return ResponseEntity.ok(userService.deleteResume(id));
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping(value = "/user/{id}/resume/create")
    ResponseEntity<?> createResume(@PathVariable Integer id, @RequestBody ResumeDto resumeDto) {
        if (!Objects.equals(AuthController.getCurrentUserId(), id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            resumeService.createResume(resumeDto, id);
        } catch (Exception e) {
            System.out.println(Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return getResume(id);
    }
}
