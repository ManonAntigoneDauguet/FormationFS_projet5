package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.mapper.TeacherMapperImpl;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class TeacherControllerUnitTest {

    @Mock
    TeacherService teacherService;

    TeacherController controller;

    @BeforeEach
    public void setUp() {
        TeacherMapper teacherMapper = new TeacherMapperImpl();
        controller = new TeacherController(teacherService, teacherMapper);
    }

    @DisplayName("Given a teacher with a X id, when a GET request is made to '/api/teacher/X', then a 200 status and a TeacherDto is returned")
    @Test
    void testSuccessGetTeacherById() throws Exception {
        // Given
        Teacher mockTeacher = new Teacher();
        mockTeacher.setFirstName("Joe");
        mockTeacher.setLastName("Tribbiani");
        Mockito.when(teacherService.findById(1L)).thenReturn(mockTeacher);
        // WHEN
        ResponseEntity<?> response = controller.findById(String.valueOf(1));
        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TeacherDto.class, Objects.requireNonNull(response.getBody()).getClass());
    }

    @DisplayName("Given no teacher with a X id, when a GET request is made to '/api/teacher/X', then a 404 status is returned")
    @Test
    void testUnfoundGetTeacherById() throws Exception {
        // Given
        Mockito.when(teacherService.findById(1L)).thenReturn(null);
        // WHEN
        ResponseEntity<?> response = controller.findById(String.valueOf(1));
        // THEN
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}