package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    String userJson = "{\"lastName\":\"toto\",\"firstName\":\"toto\",\"email\":\"toto3@toto.com\",\"password\":\"test!1234\"}}";

    String loginJson = "{\"email\":\"yoga@studio.com\",\"password\":\"test!1234\"}";

    @AfterEach
    void resetSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @DisplayName("Given a saved user, when a POST request is made to '/api/auth/login', then the user is connected")
    @Test
    void testLogin() throws Exception {
        mockMvc.perform(post("/api/auth/login")
                        .contentType("application/json")
                        .content(loginJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());
    }

    @DisplayName("Given new user data, when it send to the POST request made to '/api/auth/register', then the new user is saved")
    @Test
    void testRegister() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(userJson))
                .andExpect(status().isOk());
        Optional<User> user = userRepository.findByEmail("toto3@toto.com");
        assertTrue(user.isPresent());
    }
}