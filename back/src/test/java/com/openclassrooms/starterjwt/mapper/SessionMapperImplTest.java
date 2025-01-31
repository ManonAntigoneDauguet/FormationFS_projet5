package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SessionMapperImplTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    private SessionMapperImpl sessionMapper;

    SessionDto sessionDto;

    Session session;

    User user;

    Teacher teacher;

    Date date = new Date(1672560000000L);

    LocalDateTime localDate = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

    @BeforeEach
    void setUp() {
        sessionMapper = new SessionMapperImpl();
        sessionMapper.teacherService = teacherService;
        sessionMapper.userService = userService;

        // A user
        user = new User();
        user.setId(1L);
        user.setEmail("test@test.fr");
        user.setLastName("Green");
        user.setFirstName("Rachel");
        user.setPassword("password");
        user.setAdmin(false);
        user.setCreatedAt(localDate);
        user.setUpdatedAt(localDate);

        // A teacher
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Buffay");
        teacher.setFirstName("Phoebe");
        teacher.setCreatedAt(localDate);
        teacher.setUpdatedAt(localDate);

        // A session dto
        sessionDto = new SessionDto();
        sessionDto.setId(3L);
        sessionDto.setName("Yoga Session");
        sessionDto.setDate(date);
        sessionDto.setTeacher_id(1L);
        sessionDto.setDescription("This is a relaxing yoga session for beginners.");
        sessionDto.setUsers(Arrays.asList(1L));
        sessionDto.setCreatedAt(localDate);
        sessionDto.setUpdatedAt(localDate);

        // A session
        session = new Session();
        session.setId(3L);
        session.setName("Advanced Pilates Session");
        session.setDate(date);
        session.setDescription("An advanced Pilates session for experienced practitioners.");
        session.setCreatedAt(localDate);
        session.setUpdatedAt(localDate);
        session.setUsers(Arrays.asList(user));
        session.setTeacher(teacher);
    }

    /************************* toEntity() ****************************/

    @Test
    void testSuccessToEntity() {
        // Given
        Mockito.when(teacherService.findById(1L)).thenReturn(teacher);
        Mockito.when(userService.findById(1L)).thenReturn(user);
        // When
        Session convertedSession = sessionMapper.toEntity(sessionDto);
        // Then
        assertNotNull(convertedSession);
        assertEquals(session, convertedSession);
        assertEquals("Yoga Session", convertedSession.getName());
        assertEquals(date, convertedSession.getDate());
        assertEquals(teacher, convertedSession.getTeacher());
        assertEquals("This is a relaxing yoga session for beginners.", convertedSession.getDescription());
        assertEquals(Arrays.asList(user), convertedSession.getUsers());
        assertEquals(localDate, convertedSession.getCreatedAt());
        assertEquals(localDate, convertedSession.getUpdatedAt());
    }

    @Test
    void testNullToEntity() {
        // When
        Session convertedSession = sessionMapper.toEntity((SessionDto) null);
        // Then
        assertNull(convertedSession);
    }

    @Test
    void testSuccessListToEntity() {
        // Given
        Mockito.when(teacherService.findById(1L)).thenReturn(teacher);
        // When
        List<Session> convertedSessions = sessionMapper.toEntity(Arrays.asList(sessionDto, sessionDto));
        // Then
        assertNotNull(convertedSessions);
        assertEquals(2, convertedSessions.size());
        assertEquals("Phoebe", convertedSessions.get(0).getTeacher().getFirstName());
    }

    @Test
    void testNullListToEntity() {
        // When
        List<Session> convertedSessions = sessionMapper.toEntity((List<SessionDto>) null);
        // Then
        assertNull(convertedSessions);
    }

    /************************* toDto() ****************************/

    @Test
    void testSuccessToDto() {
        // When
        SessionDto convertedSession = sessionMapper.toDto(session);
        // Then
        assertNotNull(convertedSession);
        assertEquals(3L, convertedSession.getId());
        assertEquals("Advanced Pilates Session", convertedSession.getName());
        assertEquals(date, convertedSession.getDate());
        assertEquals(1L, convertedSession.getTeacher_id());
        assertEquals("An advanced Pilates session for experienced practitioners.", convertedSession.getDescription());
        assertEquals(Arrays.asList(1L), convertedSession.getUsers());
        assertEquals(localDate, convertedSession.getCreatedAt());
        assertEquals(localDate, convertedSession.getUpdatedAt());
    }

    @Test
    void testNullToDto() {
        // When
        SessionDto convertedSession = sessionMapper.toDto((Session) null);
        // Then
        assertNull(convertedSession);
    }

    @Test
    void testSuccessListToDto() {
        // When
        List<SessionDto> convertedSessions = sessionMapper.toDto(Arrays.asList(session, session));
        // Then
        assertNotNull(convertedSessions);
        assertEquals(2, convertedSessions.size());
        assertEquals(1L, convertedSessions.get(0).getTeacher_id());
    }

    @Test
    void testNullListToDto() {
        // When
        List<SessionDto> convertedSessions = sessionMapper.toDto((List<Session>) null);
        // Then
        assertNull(convertedSessions);
    }
}