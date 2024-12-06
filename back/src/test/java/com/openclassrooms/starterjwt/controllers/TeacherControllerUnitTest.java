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

    TeacherController teacherController;

    @BeforeEach
    public void setUp() {
        TeacherMapper teacherMapper = new TeacherMapperImpl();
        teacherController = new TeacherController(teacherService, teacherMapper);
    }

    @DisplayName("Given a teacher with a X id, when teacherController.findById(X) is called', then a 200 status and a TeacherDto is returned")
    @Test
    void testSuccessGetTeacherById() {
        // Given
        Teacher mockTeacher = new Teacher();
        mockTeacher.setFirstName("Joe");
        mockTeacher.setLastName("Tribbiani");
        Mockito.when(teacherService.findById(1L)).thenReturn(mockTeacher);
        // When
        ResponseEntity<?> response = teacherController.findById(String.valueOf(1));
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TeacherDto.class, Objects.requireNonNull(response.getBody()).getClass());
    }

    @DisplayName("Given no teacher with a X id, when teacherController.findById(X), then a 404 status is returned")
    @Test
    void testUnFoundGetTeacherById() {
        // Given
        Mockito.when(teacherService.findById(1L)).thenReturn(null);
        // When
        ResponseEntity<?> response = teacherController.findById(String.valueOf(1));
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}