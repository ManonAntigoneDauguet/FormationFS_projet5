package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class SessionServiceUnitTest {

    @Mock
    SessionRepository sessionRepository;

    @Mock
    UserRepository userRepository;

    SessionService sessionService;

    Session session;

    User user;

    @BeforeEach
    void setUp() {
        sessionService = new SessionService(sessionRepository, userRepository);

        // A session
        session = new Session();
        session.setId(1L);
        session.setName("Yoga pour débutants");
        session.setDescription("description");
        session.setDate(new Date());

        // A user
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.fr");
        user.setFirstName("Joe");
        user.setLastName("Tribbiani");
        user.setPassword("password");
        user.setAdmin(false);
    }

    /********  sessionController.participate() ***************/

    @DisplayName("Given a X session and a Y user, when sessionService.participate(X, Y), then a 200 status is returned")
    @Test
    void testSuccessParticipate() {
        // Given
        List<User> users = new ArrayList<>();
        Session session = mock(Session.class);
        Mockito.when(session.getUsers()).thenReturn(users);
        Mockito.when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        // When
        sessionService.participate(1L, 1L);
        // Then
        Mockito.verify(sessionRepository, times(1)).findById(1L);
        Mockito.verify(userRepository, times(1)).findById(1L);
        Mockito.verify(sessionRepository, times(1)).save(session);
        assertTrue(users.contains(user));
    }

    @DisplayName("Given a non-existent X session and a non-existent Y user, when sessionService.participate(X, Y), then an exception is thrown")
    @Test
    void testUnFoundParticipate() {
        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
        // Then
        assertNotNull(exception);
    }

    @DisplayName("Given a X session with a Y user who already participate, when sessionService.participate(X, Y), then an exception is thrown")
    @Test
    void testBadRequestParticipate() {
        // Given
        List<User> users = new ArrayList<>();
        users.add(user);
        Session session = mock(Session.class);
        Mockito.when(session.getUsers()).thenReturn(users);
        Mockito.when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(user));
        // When
        BadRequestException exception = assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
        // Then
        assertNotNull(exception);
    }

    /********  sessionController.noLongerParticipate() ***************/

    @DisplayName("Given a X session and a Y user who participate, when sessionService.noLongerParticipate(X, Y), then a 200 status is returned")
    @Test
    void testSuccessNoLongerParticipate() {
        // Given
        List<User> users = new ArrayList<>();
        users.add(user);
        session.setUsers(users);
        Mockito.when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        // When
        sessionService.noLongerParticipate(1L, 1L);
        // Then
        Mockito.verify(sessionRepository, times(1)).findById(1L);
        Mockito.verify(sessionRepository, times(1)).save(session);
        List<User> updatedUsers = session.getUsers();
        assertFalse(updatedUsers.contains(user));
    }

    @DisplayName("Given a X session with a Y user who not participate, when sessionService.participate(X, Y), then an exception is thrown")
    @Test
    void testBadRequestNoLongerParticipate() {
        // Given
        session.setUsers(new ArrayList<>());
        Mockito.when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        // When
        BadRequestException exception = assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        // Then
        assertNotNull(exception);
    }

    @DisplayName("Given a X session and a non-existent Y user, when sessionService.participate(X, Y), then an exception is thrown")
    @Test
    void testUnFoundNoLongerParticipate() {
        // Given
        Mockito.when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        // When
        NotFoundException exception = assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        // Then
        assertNotNull(exception);
    }
}