package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private static User littleUser;

    private static User user1;

    private static User user2;

    private static User user3;

    LocalDateTime localDate = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

    @BeforeEach
    void setUp() {
        littleUser = new User("rachel@test.fr", "Green", "Rachel", "password1", false);

        user1 = new User(1L, "rachel@test.fr", "Green", "Rachel", "password1", false, localDate, localDate);
        user2 = new User(2L, "phoebe@test.fr", "Phoebe", "Buffay", "password2", true, localDate, localDate);
        user3 = new User(2L, "phalange@test.fr", "P", "Phalange", "password3", false, localDate, localDate);
    }

    @Test
    void testGetOnLargeConstructor() {
        assertEquals(1L, user1.getId());
        assertEquals("rachel@test.fr", user1.getEmail());
        assertEquals("Green", user1.getLastName());
        assertEquals("Rachel", user1.getFirstName());
        assertEquals("password1", user1.getPassword());
        assertEquals(false, user1.isAdmin());
        assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0, 0), user1.getUpdatedAt());
        assertEquals(LocalDateTime.of(2023, 1, 1, 12, 0, 0), user1.getCreatedAt());
    }

    @Test
    void testGetOnLittleConstructor() {
        assertEquals("rachel@test.fr", littleUser.getEmail());
        assertEquals("Green", littleUser.getLastName());
        assertEquals("Rachel", littleUser.getFirstName());
        assertEquals("password1", littleUser.getPassword());
        assertEquals(false, littleUser.isAdmin());
    }

    @Test
    void testSuccessSet() {
        // When
        user1.setUpdatedAt(LocalDateTime.of(2024, 2, 1, 12, 0, 0));
        user1.setLastName("Buffay");
        user1.setEmail("phoebe@test.fr");
        user1.setFirstName("Phoebe");
        user1.setPassword("new password");
        // Then
        assertEquals("phoebe@test.fr", user1.getEmail());
        assertEquals("Buffay", user1.getLastName());
        assertEquals("Phoebe", user1.getFirstName());
        assertEquals("new password", user1.getPassword());
        assertEquals(LocalDateTime.of(2024, 2, 1, 12, 0, 0), user1.getUpdatedAt());
    }

    @Test
    void testErrorSet() {
        assertThrows(NullPointerException.class, () -> user1.setEmail(null));
        assertThrows(NullPointerException.class, () -> user1.setPassword(null));
        assertThrows(NullPointerException.class, () -> user1.setLastName(null));
        assertThrows(NullPointerException.class, () -> user1.setFirstName(null));
    }

    @Test
    void testErrorConstructor() {
        assertThrows(NullPointerException.class, () -> new User(null, "Green", "Rachel", "password", true));
        assertThrows(NullPointerException.class, () -> new User("rachel@test.fr", null, "Rachel", "password", true));
        assertThrows(NullPointerException.class, () -> new User("rachel@test.fr", "Green", null, "password", true));
        assertThrows(NullPointerException.class, () -> new User("rachel@test.fr", "Green", "Rachel", null, true));
    }

    @Test
    void testToString() {
        String expected = "User(id=1, email=rachel@test.fr, lastName=Green, firstName=Rachel, password=password1, admin=false, createdAt=2023-01-01T12:00, updatedAt=2023-01-01T12:00)";
        assertEquals(expected, user1.toString());
    }

    /************************* equals() & hashCode() ****************************/

    @Test
    void testSuccessEquals() {
        // When
        boolean result = user1.equals(user1);
        // Then
        assertTrue(result);
    }

    @Test
    void testSameIdsEquals() {
        // When
        boolean result = user2.equals(user3);
        // Then
        assertTrue(result);
        assertEquals(user2.hashCode(), user3.hashCode());
    }

    @Test
    void testNullObjectEquals() {
        // When
        boolean result = user1.equals(null);
        // Then
        assertFalse(result);
    }

    @Test
    void testDifferentClassEquals() {
        // Given
        Object other = "Not a User";
        // When
        boolean result = user1.equals(other);
        // Then
        assertFalse(result);
    }

    @Test
    void testDifferentIdsEquals() {
        // When
        boolean result = user1.equals(user2);
        // Then
        assertFalse(result);
        assertNotEquals(user1.hashCode(), user2.hashCode());
    }
}