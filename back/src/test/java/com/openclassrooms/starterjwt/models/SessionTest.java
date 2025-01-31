package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SessionTest {

    private static User user1;

    private static User user2;

    private Teacher teacher1;

    private Teacher teacher2;

    private Session session1;

    private Session session2;

    private Session session3;

    Date date = new Date(2025, 02, 11);

    LocalDateTime localDate = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

    @BeforeEach
    void setUp() {
        user1 = new User(1L, "rachel@test.fr", "Green", "Rachel", "password1", false, localDate, localDate);
        user2 = new User(2L, "phoebe@test.fr", "Phoebe", "Buffay", "password2", true, localDate, localDate);

        teacher1 = new Teacher(1L, "Tribbiani", "Joe", localDate, localDate);
        teacher2 = new Teacher(2L, "Buffay", "Phoebe", localDate, localDate);

        session1 = new Session(1L, "Yoga pour débutants", date, "description1", teacher1, List.of(user1, user2), localDate, localDate);
        session2 = new Session(2L, "Yoga avancé", date, "description2", teacher1, List.of(user1), localDate, localDate);
        session3 = new Session(2L, "Yoga très avancé", date, "description3", teacher2, List.of(user2), localDate, localDate);
    }

    @Test
    void testToString() {
        String expected = "Session(id=1, name=Yoga pour débutants, date=Wed Mar 11 00:00:00 CET 3925, description=description1, teacher=Teacher(id=1, lastName=Tribbiani, firstName=Joe, createdAt=2023-01-01T12:00, updatedAt=2023-01-01T12:00), users=[User(id=1, email=rachel@test.fr, lastName=Green, firstName=Rachel, password=password1, admin=false, createdAt=2023-01-01T12:00, updatedAt=2023-01-01T12:00), User(id=2, email=phoebe@test.fr, lastName=Phoebe, firstName=Buffay, password=password2, admin=true, createdAt=2023-01-01T12:00, updatedAt=2023-01-01T12:00)], createdAt=2023-01-01T12:00, updatedAt=2023-01-01T12:00)";
        assertEquals(expected, session1.toString());
    }

    /************************* equals() & hashCode() ****************************/

    @Test
    void testSuccessEquals() {
        // When
        boolean result = session1.equals(session1);
        // Then
        assertTrue(result);
    }

    @Test
    void testNotEquals() {
        // When
        boolean result = session1.equals(session3);
        // Then
        assertFalse(result);
        assertNotEquals(session1.hashCode(), session3.hashCode());
    }

    @Test
    void testSameIdsEquals() {
        // When
        boolean result = session2.equals(session3);
        // Then
        assertTrue(result);
        assertEquals(session2.hashCode(), session3.hashCode());
    }

    @Test
    void testNullObjectEquals() {
        // When
        boolean result = session1.equals(null);
        // Then
        assertFalse(result);
    }

    @Test
    void testDifferentClassEquals() {
        // Given
        Object other = "Not a Session";
        // When
        boolean result = session1.equals(other);
        // Then
        assertFalse(result);
    }

    @Test
    void testDifferentIdsEquals() {
        // When
        boolean result = session1.equals(session2);
        // Then
        assertFalse(result);
        assertNotEquals(session1.hashCode(), session2.hashCode());
    }
}