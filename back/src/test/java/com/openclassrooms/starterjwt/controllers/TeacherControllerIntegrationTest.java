package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    @DisplayName("Given a teacher saved into the database and a authenticated user, when a GET request is made to '/api/teacher/1', then the teacher information is returned")
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void testGetTeacherById() throws Exception {
        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Drake"));
    }

    @DisplayName("Given two teachers saved into the database and a authenticated user, when a GET request is made to '/api/teacher', then the information of the two teachers is returned")
    @Test
    @WithMockUser(username = "yoga@studio.com", roles = {"ADMIN"})
    void testGetAllTeacher() throws Exception {
        mockMvc.perform(get("/api/teacher"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Drake"))
                .andExpect(jsonPath("$[1].firstName").value("Phoebe"));
    }
}