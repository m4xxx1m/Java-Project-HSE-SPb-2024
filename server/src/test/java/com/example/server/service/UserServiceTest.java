package com.example.server.service;

import com.example.server.dto.UserUpdateDto;
import com.example.server.model.User;
import com.example.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import com.example.server.model.Role;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    public void testUpdateUser() throws IOException {
        UserUpdateDto updateDto = new UserUpdateDto();
        updateDto.setUsername("newUsername");
        updateDto.setEmail("newEmail");
        updateDto.setPassword("newPassword");
//        updateDto.setProfilePictureUrl("newProfilePictureUrl");
        updateDto.setContacts("newContacts");
        updateDto.setBio("newBio");
//        updateDto.setResumeUrl("newResumeUrl");
        updateDto.setTags("newTags");

        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User result = userService.updateUser(1, updateDto);

        assertEquals(updateDto.getUsername(), result.getUsername());
        assertEquals(updateDto.getEmail(), result.getEmail());
        assertEquals(updateDto.getPassword(), result.getPassword());
//        assertEquals(updateDto.getProfilePictureUrl(), result.getProfilePictureUrl());
        assertEquals(updateDto.getContacts(), result.getContacts());
        assertEquals(updateDto.getBio(), result.getBio());
//        assertEquals(updateDto.getResumeUrl(), result.getResumeUrl());
        assertEquals(updateDto.getTags(), result.getTags());
    }

    @Test
    public void testUpdateUserContacts() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        userService.updateUserContacts(1, "newContacts");

        assertEquals("newContacts", user.getContacts());
    }

    @Test
    public void testUpdateUserBio() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        userService.updateUserBio(1, "newBio");

        assertEquals("newBio", user.getBio());
    }

    @Test
    public void testUpdateUserTags() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        userService.updateUserTags(1, "newTags");

        assertEquals("newTags", user.getTags());
    }

    @Test
    public void testGetUser() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getUser(1);

        assertEquals(user, result);
    }

    @Test
    public void testSave() {
        User user = new User();
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        User result = userService.save(user);

        assertEquals(user, result);
    }

    @Test
    public void testCreate() {
        User user = new User();
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
        User result = userService.create(user);
        assertEquals(user, result);
    }

    @Test
    public void testGetByUsername() {
        User user = new User();
        when(userRepository.findByUsername("username")).thenReturn(user);
        User result = userService.getByUsername("username");
        assertEquals(user, result);
    }

    @Test
    public void testUpdateUsername() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        userService.updateUsername(1, "newUsername");

        assertEquals("newUsername", user.getUsername());
    }

    @Test
    public void testUpdateEmail() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        userService.updateEmail(1, "newEmail");

        assertEquals("newEmail", user.getEmail());
    }

    @Test
    public void testUpdatePassword() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        userService.updatePassword(1, "newPassword");

        assertEquals("newPassword", user.getPassword());
    }

    @Test
    public void testUpdateProfilePictureUrl() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        userService.updateProfilePictureUrl(1, "newProfilePictureUrl");

        assertEquals("newProfilePictureUrl", user.getProfilePictureUrl());
    }

//    @Test
//    public void testUpdateResumeUrl() {
//        User user = new User();
//        when(userRepository.findById(1)).thenReturn(Optional.of(user));
//        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);
//
//        userService.updateResumeUrl(1, "newResumeUrl");
//
//        assertEquals("newResumeUrl", user.getResumeUrl());
//    }

    @Test
    public void testUpdateRole() {
        User user = new User();
        when(userRepository.findById(1)).thenReturn(Optional.of(user));
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        userService.updateRole(1, Role.ROLE_ADMIN);

        assertEquals(Role.ROLE_ADMIN, user.getRole());
    }

    @Test
    public void testGetUsersList() {
        User user1 = new User();
        User user2 = new User();
        Set<Integer> userIds = new HashSet<>(Arrays.asList(1, 2));
        when(userRepository.findAllById(userIds)).thenReturn(Arrays.asList(user1, user2));

        List<User> result = userService.getUsersList(userIds);

        assertEquals(2, result.size());
        assertTrue(result.contains(user1));
        assertTrue(result.contains(user2));
    }

    @Test
    public void testGetUserNotFound() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        User result = userService.getUser(1);

        assertNull(result);
    }

    @Test
    public void testCreateUserWithExistingUsername() {
        User user = new User();
        user.setUsername("existingUsername");
        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(user));
    }

    @Test
    public void testCreateUserWithExistingEmail() {
        User user = new User();
        user.setEmail("existingEmail");
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(RuntimeException.class, () -> userService.create(user));
    }

    @Test
    public void testGetByUsernameNotFound() {
        when(userRepository.findByUsername("nonExistingUsername")).thenReturn(null);

        User result = userService.getByUsername("nonExistingUsername");

        assertNull(result);
    }

    @Test
    public void testUpdateNonExistingUser() {
        UserUpdateDto updateDto = new UserUpdateDto();
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> userService.updateUser(1, updateDto));
    }
}