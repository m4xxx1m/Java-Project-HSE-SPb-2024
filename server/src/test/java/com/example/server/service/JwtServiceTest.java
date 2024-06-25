package com.example.server.service;

import com.example.server.model.Role;
import com.example.server.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private User user;

    private String token;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        when(user.getUsername()).thenReturn("testUser");
        when(user.getUserId()).thenReturn(1);
        when(user.getEmail()).thenReturn("test@example.com");
        when(user.getRole()).thenReturn(Role.valueOf("ROLE_USER"));
        token = jwtService.generateToken(user);
    }

    @Test
    public void testExtractUserName() {
        String userName = jwtService.extractUserName(token);
        assertEquals("testUser", userName);
    }

    @Test
    public void testGenerateToken() {
        String generatedToken = jwtService.generateToken(user);
        assertEquals(token, generatedToken);
    }

    @Test
    public void testIsTokenValid() {
        boolean isValid = jwtService.isTokenValid(token, user);
        assertTrue(isValid);
    }

    @Test
    public void testIsTokenExpired() {
        boolean isExpired = jwtService.isTokenExpired(token);
        assertFalse(isExpired);
    }

    @Test
    public void testExtractExpiration() {
        Date expiration = jwtService.extractExpiration(token);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    public void testExtractAllClaims() {
        Claims claims = jwtService.extractAllClaims(token);
        assertEquals("testUser", claims.getSubject());
        assertEquals(1L, claims.get("id"));
        assertEquals("test@example.com", claims.get("email"));
        assertEquals("ROLE_USER", claims.get("role"));
    }

    @Test
    public void testIsTokenValidWithInvalidToken() {
        boolean isValid = jwtService.isTokenValid("invalidToken", user);
        assertFalse(isValid);
    }

    @Test
    public void testIsTokenValidWithWrongUserDetails() {
        User wrongUser = new User();
        wrongUser.setUsername("wrongUser");
        wrongUser.setPassword("password");
        wrongUser.setRole(Role.valueOf("ROLE_USER"));
        boolean isValid = jwtService.isTokenValid(token, wrongUser);
        assertFalse(isValid);
    }

    @Test
    public void testExtractUserNameWithInvalidToken() {
        String userName = jwtService.extractUserName("invalidToken");
        assertNull(userName);
    }
}