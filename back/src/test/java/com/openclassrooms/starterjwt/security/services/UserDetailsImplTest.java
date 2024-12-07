package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserDetailsImplTest {

    UserDetailsImpl user1;

    UserDetailsImpl user2;

    UserDetailsImpl user3;

    @BeforeEach
    void setUp() {
        user1 = new UserDetailsImpl(3L, "joe@test.fr", "Joe", "Tribbiani", true, "password1");
        user2 = new UserDetailsImpl(4L, "phoebe@test.fr", "Phoebe", "Buffay", true, "password2");
        user3 = new UserDetailsImpl(4L, "phalange@test.fr", "P", "Phalange", false, "password3");
    }

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
        Object other = "Not a UserDetailsImpl";
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
    }
}