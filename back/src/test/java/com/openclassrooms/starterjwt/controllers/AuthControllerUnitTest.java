package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.payload.response.JwtResponse;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AuthControllerUnitTest {

    @Mock
    UserRepository userRepository;

    @Mock
    JwtUtils jwtUtils;

    @Mock
    AuthenticationManager authenticationManager;

    @Mock
    PasswordEncoder passwordEncoder;

    AuthController authController;

    User user;

    UserDetailsImpl userDetails;

    LoginRequest loginRequest;

    SignupRequest signupRequest;


    @BeforeEach
    void setUp() {
        authController = new AuthController(authenticationManager, passwordEncoder, jwtUtils, userRepository);

        // A saved user
        user = new User();
        user.setEmail("test@test.fr");
        user.setFirstName("Joe");
        user.setLastName("Tribbiani");
        user.setPassword("$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq");
        user.setAdmin(false);

        // The UserDetails matching with the saved user
        userDetails = new UserDetailsImpl(3L, user.getEmail(), user.getFirstName(), user.getLastName(), true, user.getPassword());

        // The login Request
        loginRequest = new LoginRequest();
        loginRequest.setEmail("test@test.fr");
        loginRequest.setEmail("password");

        // The signup request
        signupRequest = new SignupRequest();
        signupRequest.setEmail(user.getEmail());
        signupRequest.setFirstName(user.getFirstName());
        signupRequest.setLastName(user.getLastName());
        signupRequest.setPassword("password");
    }

    @AfterEach
    void resetSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("Given a saved user, when authenticateUser is called with the correct email and password, then a 200 status and a JwtResponse is returned")
    @Test
    void testSuccessLogin() {
        // Given
        Authentication authentication = mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()))).thenReturn(authentication);
        Mockito.when(jwtUtils.generateJwtToken(authentication)).thenReturn("TOKEN");
        Mockito.when(userRepository.findByEmail(userDetails.getUsername())).thenReturn(Optional.ofNullable(user));
        // When
        ResponseEntity<?> response = authController.authenticateUser(loginRequest);
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(JwtResponse.class, Objects.requireNonNull(response.getBody()).getClass());
    }

    @DisplayName("Given invalid credentials, when authenticateUser is called, then an exception is thrown")
    @Test
    void testAuthenticationFailed() {
        AuthenticationException exception = new AuthenticationException("Authentication failed") {
        };
        // Given
        Mockito.when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()))).thenThrow(exception);
        // When
        AuthenticationException result = assertThrows(AuthenticationException.class, () -> authController.authenticateUser(loginRequest));
        // Then
        assertNotNull(result);
    }

    @DisplayName("Given new user data, when registerUser is called with his data, then a 200 status and a JwtResponse is returned")
    @Test
    void testSuccessRegister() {
        // Given
        Mockito.when(userRepository.existsByEmail(signupRequest.getEmail())).thenReturn(false);
        Mockito.when(passwordEncoder.encode(signupRequest.getPassword())).thenReturn("encodedPassword");
        // When
        ResponseEntity<?> response = authController.registerUser(signupRequest);
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("Given data of a saved user, when registerUser is called with his data, then a 400 status is return")
    @Test
    void testEmailAlreadySaved() {
        // Given
        Mockito.when(userRepository.existsByEmail(any())).thenReturn(true);
        // When
        ResponseEntity<?> response = authController.registerUser(signupRequest);
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}