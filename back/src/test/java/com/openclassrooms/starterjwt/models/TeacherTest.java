package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TeacherTest {

    private Teacher teacher1;

    private Teacher teacher2;

    private Teacher teacher3;

    LocalDateTime localDate = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

    @BeforeEach
    void setUp() {
        teacher1 = new Teacher(1L, "Tribbiani", "Joe", localDate, localDate);
        teacher2 = new Teacher(2L, "Buffay", "Phoebe", localDate, localDate);
        teacher3 = new Teacher(2L, "Phalange", "P", localDate, localDate);
    }

    @Test
    void testToString() {
        String expected = "Teacher(id=1, lastName=Tribbiani, firstName=Joe, createdAt=2023-01-01T12:00, updatedAt=2023-01-01T12:00)";
        assertEquals(expected, teacher1.toString());
    }

    /************************* equals() & hashCode() ****************************/

    @Test
    void testSuccessEquals() {
        // When
        boolean result = teacher1.equals(teacher1);
        // Then
        assertTrue(result);
    }

    @Test
    void testSameIdsEquals() {
        // When
        boolean result = teacher2.equals(teacher3);
        // Then
        assertTrue(result);
        assertEquals(teacher2.hashCode(), teacher3.hashCode());
    }

    @Test
    void testNullObjectEquals() {
        // When
        boolean result = teacher1.equals(null);
        // Then
        assertFalse(result);
    }

    @Test
    void testDifferentClassEquals() {
        // Given
        Object other = "Not a Teacher";
        // When
        boolean result = teacher1.equals(other);
        // Then
        assertFalse(result);
    }

    @Test
    void testDifferentIdsEquals() {
        // When
        boolean result = teacher1.equals(teacher2);
        // Then
        assertFalse(result);
        assertNotEquals(teacher1.hashCode(), teacher2.hashCode());
    }
}