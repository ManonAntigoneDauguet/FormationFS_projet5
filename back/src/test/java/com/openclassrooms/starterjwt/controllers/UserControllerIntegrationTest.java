package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("Given a user saved into the database and a authenticated user, when a GET request is made to '/api/user/1', then the user information is returned")
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void testGetUserById() throws Exception {
        mockMvc.perform(get("/api/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("yoga@studio.com"));
    }

    @DisplayName("Given a user saved into the database and a authenticated user, when a DELETE request is made to '/api/user/1', then the user is removed")
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void testDeleteUser() throws Exception {
        Optional<User> oldUser = userRepository.findById(1L);
        assertTrue(oldUser.isPresent());

        mockMvc.perform(delete("/api/user/1"))
                .andExpect(status().isOk());
        Optional<User> user = userRepository.findById(1L);
        assertTrue(user.isEmpty());
    }
}