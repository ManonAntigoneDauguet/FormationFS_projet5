package com.openclassrooms.starterjwt.payload.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

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
    void testSuccessSet() {
        request1.setEmail("newEmail");
        assertEquals("newEmail", request1.getEmail());

        request1.setFirstName("newFirstName");
        assertEquals("newFirstName", request1.getFirstName());

        request1.setLastName("newLastName");
        assertEquals("newLastName", request1.getLastName());

        request1.setPassword("newPassword");
        assertEquals("newPassword", request1.getPassword());
    }

    @Test
    void testToString() {
        String expected = "SignupRequest(email=joe@example.com, firstName=Joe, lastName=Tribbani, password=password1)";
        assertEquals(expected, request1.toString());
    }

    /************************* equals() & hashCode() ****************************/

    @Test
    void testSuccessEquals() {
        assertEquals(request1, request1);
        assertEquals(request1.hashCode(), request1.hashCode());

        assertEquals(request1, request2);
        assertEquals(request1.hashCode(), request2.hashCode());
    }

    @Test
    void testDifferentFieldEquals() {
        assertNotEquals(request1, request3);
        assertNotEquals(request1.hashCode(), request3.hashCode());

        request1.setFirstName("Phoebe");
        request1.setLastName("Buffay");
        request1.setEmail("phoebe@example.com");
        request1.setPassword("password2");
        assertEquals(request1, request3);
        assertEquals(request1.hashCode(), request3.hashCode());
    }

    @Test
    void testNullObjectEquals() {
        assertNotEquals(request1, null);
    }

    @Test
    void testEqualsWithNullFields() {
        request1.setEmail(null);
        request2.setEmail(null);
        assertEquals(request1, request2);
    }

    @Test
    void testDifferentClassEquals() {
        Object other = "Not a request";
        assertNotEquals(request1, other);
        assertNotEquals(request1.hashCode(), other.hashCode());
    }
}