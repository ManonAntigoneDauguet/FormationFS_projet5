package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SignupRequestTest {

    SignupRequest request1;

    SignupRequest request2;

    SignupRequest request3;


    @BeforeEach
    void setUp() {
        request1 = new SignupRequest();
        request1.setEmail("joe@example.com");
        request1.setFirstName("Joe");
        request1.setLastName("Tribbani");
        request1.setPassword("password1");

        request2 = new SignupRequest();
        request2.setEmail("joe@example.com");
        request2.setFirstName("Joe");
        request2.setLastName("Tribbani");
        request2.setPassword("password1");

        request3 = new SignupRequest();
        request3.setEmail("phoebe@example.com");
        request3.setFirstName("Phoebe");
        request3.setLastName("Buffay");
        request3.setPassword("password2");
    }

    @Test
    void testSetEmail() {
        request1.setEmail("newEmail");
        assertEquals("newEmail", request1.getEmail());
    }

    @Test
    void testSetFirstName() {
        request1.setFirstName("newFirstName");
        assertEquals("newFirstName", request1.getFirstName());
    }

    @Test
    void testSetLastName() {
        request1.setLastName("newLastName");
        assertEquals("newLastName", request1.getLastName());
    }

    @Test
    void testSetPassword() {
        request1.setPassword("newPassword");
        assertEquals("newPassword", request1.getPassword());
    }

    @Test
    void testEquals() {
        assertEquals(request1, request2);
        assertNotEquals(request1, request3);
    }

    @Test
    void canEqual() {
        assertTrue(request1.canEqual(request3));
        assertFalse(request1.canEqual(new Object()));
    }

    @Test
    void testHashCode() {
        assertEquals(request1.hashCode(), request2.hashCode());
        assertNotEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testToString() {
        String result = request1.toString();
        assertTrue(result.contains("SignupRequest"));
        assertTrue(result.contains("joe@example.com"));
        assertTrue(result.contains("Joe"));
        assertTrue(result.contains("Tribbani"));
        assertTrue(result.contains("password1"));
    }
}