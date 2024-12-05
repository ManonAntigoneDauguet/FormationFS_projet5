package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.mapper.UserMapperImpl;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {

    User user;

    User authenticatedUser;

    String password = "$2a$10$.Hsa/ZjUVaHqi0tp9xieMeewrnZxrZ5pQRzddUXE/WjDu2ZThe6Iq";

    @Mock
    UserService userService;

    UserController userController;

    @BeforeEach
    public void setUp() {
        UserMapper userMapper = new UserMapperImpl();
        userController = new UserController(userService, userMapper);

        // An X unauthenticated user
        user = new User();
        user.setEmail("test@test.fr");
        user.setFirstName("Joe");
        user.setLastName("Tribbiani");
        user.setPassword(password);
        user.setAdmin(false);

        // An Y authenticated user
        authenticatedUser = new User();
        authenticatedUser.setEmail("authenticated@test.fr");
        authenticatedUser.setFirstName("Phoebe");
        authenticatedUser.setLastName("Buffay");
        authenticatedUser.setPassword(password);
        authenticatedUser.setAdmin(false);

        // Authentication of the Y user
        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("authenticated@test.fr")
                .password(password)
                .roles("USER")
                .build();
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()));
        SecurityContextHolder.setContext(securityContext);
    }

    @AfterEach
    void resetSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("Given a user with a X id, when userController.findById(X) is called, then a 200 status and a UserDto is returned")
    @Test
    void getSuccessGetUserByID() {
        // Given
        Mockito.when(userService.findById(1L)).thenReturn(user);
        // When
        ResponseEntity<?> response = userController.findById("1");
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UserDto.class, Objects.requireNonNull(response.getBody()).getClass());
    }

    @DisplayName("Given no user with a X id, when userController.findById(X), then a 404 status is returned")
    @Test
    void testUnFoundGetTeacherById() {
        // Given
        Mockito.when(userService.findById(1L)).thenReturn(null);
        // When
        ResponseEntity<?> response = userController.findById("1");
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @DisplayName("Given a authenticated user with a X id, when userController.save(X), then a 200 status is returned")
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void getSuccessDelete() {
        // Given
        Mockito.when(userService.findById(1L)).thenReturn(authenticatedUser);
        // When
        ResponseEntity<?> response = userController.save("1");
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("Given a user with a X id, when a DELETED request userController.save(Y), then a 401 status is returned")
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void getUnauthorizedDelete() {
        // Given
        Mockito.when(userService.findById(1L)).thenReturn(user);
        // When
        ResponseEntity<?> response = userController.save("1");
        // Then
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}