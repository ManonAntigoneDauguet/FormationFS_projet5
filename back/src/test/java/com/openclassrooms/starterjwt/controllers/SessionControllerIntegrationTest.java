package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class SessionControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SessionRepository sessionRepository;

    String sessionJson = "{\"name\":\"Yoga Sexy avec Joe\",\"date\":\"2024-12-15T10:30:00\",\"teacher_id\":1,\"description\":\"description\"}";


    @DisplayName("Given a session saved into the database and a authenticated user, when a GET request is made to '/api/session/1', then the session information is returned")
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void testGetSessionByID() throws Exception {
        mockMvc.perform(get("/api/session/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga pour débutants"));
    }

    @DisplayName("Given two sessions saved into the database and a authenticated user, when a GET request is made to '/api/session', then the information of the two sessions is returned")
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void testGetAllSessions() throws Exception {
        mockMvc.perform(get("/api/session"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Yoga pour débutants"))
                .andExpect(jsonPath("$[1].name").value("Yoga avancé"));
    }

    @DisplayName("Given a new session data, when a POST request is made to '/api/session', then the new session is saved")
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void testCreateSession() throws Exception {
        mockMvc.perform(post("/api/session")
                        .contentType("application/json")
                        .content(sessionJson))
                .andExpect(status().isOk());
        Optional<Session> session = sessionRepository.findById(3L);
        assertTrue(session.isPresent());
    }

    @DisplayName("Given a session saved into the database with an X id and a authenticated user, when a PUT request is made to '/api/session/X' with new information, then the session information is updated")
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void testUpdateSession() throws Exception {
        Optional<Session> oldSession = sessionRepository.findById(1L);
        assertTrue(oldSession.isPresent());
        String name = oldSession.get().getName();
        assertEquals("Yoga pour débutants", name);

        mockMvc.perform(put("/api/session/1")
                        .contentType("application/json")
                        .content(sessionJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Sexy avec Joe"));
    }

    @DisplayName("Given a session saved into the database with an X id and a authenticated user, when a DELETE request is made to '/api/session/X', then the session is removed")
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void testDeleteSession() throws Exception {
        Optional<Session> oldSession = sessionRepository.findById(1L);
        assertTrue(oldSession.isPresent());

        mockMvc.perform(delete("/api/session/1"))
                .andExpect(status().isOk());
        Optional<Session> session = sessionRepository.findById(1L);
        assertTrue(session.isEmpty());
    }
}