package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {

    JwtResponse jwtResponse;

    @BeforeEach
    void setUp() {
        jwtResponse = new JwtResponse(
                "accessToken", 1L, "username", "firstName", "lastName", true
        );
    }

    @Test
    void setToken() {
        jwtResponse.setToken("newToken");
        assertEquals("newToken", jwtResponse.getToken());
    }

    @Test
    void setType() {
        jwtResponse.setType("NoBearer");
        assertEquals("NoBearer", jwtResponse.getType());
    }

    @Test
    void setId() {
        jwtResponse.setId(3L);
        assertEquals(3L, jwtResponse.getId());
    }

    @Test
    void setUsername() {
        jwtResponse.setUsername("newUsername");
        assertEquals("newUsername", jwtResponse.getUsername());
    }

    @Test
    void setFirstName() {
        jwtResponse.setFirstName("newFirstName");
        assertEquals("newFirstName", jwtResponse.getFirstName());
    }

    @Test
    void setLastName() {
        jwtResponse.setLastName("newLastName");
        assertEquals("newLastName", jwtResponse.getLastName());
    }

    @Test
    void setAdmin() {
        jwtResponse.setAdmin(false);
        assertEquals(false, jwtResponse.getAdmin());
    }
}