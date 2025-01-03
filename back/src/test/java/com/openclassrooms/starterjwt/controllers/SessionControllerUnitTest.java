package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.mapper.SessionMapperImpl;
import com.openclassrooms.starterjwt.models.Session;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class SessionControllerUnitTest {

    @Mock
    SessionService sessionService;

    SessionController sessionController;

    Session session;

    SessionDto sessionDto;

    String invalidId = "invalidId";

    String validId = "1";

    @BeforeEach
    void setUp() {
        SessionMapper sessionMapper = new SessionMapperImpl();
        sessionController = new SessionController(sessionService, sessionMapper);

        // A session
        session = new Session();
        session.setId(1L);
        session.setName("Yoga pour débutants");
        session.setDescription("description");
        session.setDate(new Date());

        // A sessionDto
        sessionDto = new SessionDto();
    }

    /********  sessionController.findById() ***************/

    @DisplayName("Given a session with a X id, when sessionController.findById(X) is called, then a 200 status and a SessionDto is returned")
    @Test
    void testSuccessGetSessionByID() {
        // Given
        Mockito.when(sessionService.getById(1L)).thenReturn(session);
        // When
        ResponseEntity<?> response = sessionController.findById(validId);
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
        ResponseEntity<?> response = sessionController.findById(validId);
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @DisplayName("Given invalid id, when sessionController.findById(invalidId), then an 400 status is returned")
    @Test
    void testFormatExceptionGetSessionByID() {
        // When
        ResponseEntity<?> response = sessionController.findById(invalidId);
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    /********  sessionController.findById() ***************/

    @DisplayName("Given a two sessions, when sessionController.findAll(), then a 200 status is returned with the two sessions")
    @Test
    void testSuccessGetAllSessions() {
        // Given
        List<Session> sessions = new ArrayList<>();
        sessions.add(session);
        sessions.add(session);
        Mockito.when(sessionService.findAll()).thenReturn(sessions);
        // When
        ResponseEntity<?> response = sessionController.findAll();
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<Session> resultList = (List<Session>) response.getBody();
        assertEquals(ArrayList.class, Objects.requireNonNull(response.getBody()).getClass());
        assertNotNull(sessions);
        assertEquals(2, Objects.requireNonNull(resultList).size());
    }

    /********  sessionController.participate() ***************/

    @DisplayName("Given a X session and a Y user, when sessionController.participate(X, Y), then a 200 status is returned")
    @Test
    void testSuccessParticipate() {
        // Given
        Mockito.doNothing().when(sessionService).participate(1L, 1L);
        // When
        ResponseEntity<?> response = sessionController.participate(validId, validId);
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("Given a X session with a Y user who already participate, when sessionController.participate(X, Y), then an exception is thrown")
    @Test
    void testBadRequestParticipate() {
        // Given
        Mockito.doThrow(new BadRequestException()).when(sessionService).participate(1L, 1L);
        // When
        BadRequestException exception = assertThrows(BadRequestException.class, () -> sessionController.participate(validId, validId));
                // Then
        assertNotNull(exception);
    }

    @DisplayName("Given a X session and a non-existent Y user, when sessionController.participate(X, Y), then an exception is thrown")
    @Test
    void testUnFoundParticipate() {
        // Given
        Mockito.doThrow(new NotFoundException()).when(sessionService).participate(1L, 1L);
        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> sessionController.participate(validId, validId));
        // Then
        assertNotNull(exception);
    }

    @DisplayName("Given invalid id, when sessionController.participate(invalidX, invalidY), then an 400 status is returned")
    @Test
    void testFormatExceptionParticipate() {
        // When
        ResponseEntity<?> response = sessionController.participate(invalidId, validId);
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    /********  sessionController.noLongerParticipate() ***************/

    @DisplayName("Given a X session and a Y user who participate, when sessionController.noLongerParticipate(X, Y), then a 200 status is returned")
    @Test
    void testSuccessNoLongerParticipate() {
        // When
        ResponseEntity<?> response = sessionController.noLongerParticipate(validId, validId);
        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @DisplayName("Given a X session with a Y user who not participate, when sessionController.participate(X, Y), then an exception is thrown")
    @Test
    void testBadRequestNoLongerParticipate() {
        // Given
        Mockito.doThrow(new BadRequestException()).when(sessionService).noLongerParticipate(1L, 1L);
        // When
        BadRequestException exception = assertThrows(BadRequestException.class, () -> sessionController.noLongerParticipate(validId, validId));
        // Then
        assertNotNull(exception);
    }

    @DisplayName("Given a X session and a non-existent Y user, when sessionController.participate(X, Y), then an exception is thrown")
    @Test
    void testUnFoundNoLongerParticipate() {
        // Given
        Mockito.doThrow(new NotFoundException()).when(sessionService).noLongerParticipate(1L, 1L);
        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> sessionController.noLongerParticipate(validId, validId));
        // Then
        assertNotNull(exception);
    }

    @DisplayName("Given invalid id, when sessionController.noLongerParticipate(invalidX, invalidY), then an 400 status is returned")
    @Test
    void testFormatExceptionNoLongerParticipate() {
        // When
        ResponseEntity<?> response = sessionController.noLongerParticipate(invalidId, validId);
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    /********  sessionController.save() ***************/

    @DisplayName("Given invalid id, when sessionController.save(invalidX), then an 400 status is returned")
    @Test
    void testFormatExceptionSave() {
        // When
        ResponseEntity<?> response = sessionController.save(invalidId);
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }

    @DisplayName("Given a non-existent session with a X id, when sessionController.save(X), then an 404 status is returned")
    @Test
    void testUnFoundSave() {
        // When
        ResponseEntity<?> response = sessionController.save("2");
        // Then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    /********  sessionController.update() ***************/

    @DisplayName("Given invalid id, when sessionController.update(invalidX, invalidY), then an 400 status is returned")
    @Test
    void testFormatExceptionUpdate() {
        // When
        ResponseEntity<?> response = sessionController.update(invalidId, sessionDto);
        // Then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
    }
}