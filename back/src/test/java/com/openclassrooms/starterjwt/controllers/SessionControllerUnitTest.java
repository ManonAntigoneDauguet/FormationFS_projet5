package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SessionControllerUnitTest {

    @Mock
    SessionService sessionService;

    SessionController sessionController;

    Session session;

    @BeforeEach
    void setUp() {
        SessionMapper sessionMapper = new SessionMapperImpl();
        sessionController = new SessionController(sessionService, sessionMapper);

        session = new Session();
        session.setId(1L);
        session.setName("Yoga pour débutants");
        session.setDescription("description");
        session.setDate(new Date());
    }

    @DisplayName("Given a session with a X id, when sessionController.findById(X) is called, then a 200 status and a SessionDto is returned")
    @Test
    void testSuccessGetSessionByID() {
        // Given
        Mockito.when(sessionService.getById(1L)).thenReturn(session);
        // When
        ResponseEntity<?> response = sessionController.findById("1");
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(SessionDto.class, Objects.requireNonNull(response.getBody()).getClass());
    }

    @DisplayName("Given no session with a X id, when sessionController.findById(X), then a 404 status is returned")
    @Test
    void testUnFoundGetSessionByID() {
        // Given
        Mockito.when(sessionService.getById(1L)).thenReturn(null);
        // When
        ResponseEntity<?> response = sessionController.findById("1");
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}