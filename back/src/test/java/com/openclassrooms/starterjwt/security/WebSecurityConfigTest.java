package com.openclassrooms.starterjwt.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @DisplayName("Given a unauthenticated user, when a secured request is made, then an 401 STATUS is returned")
    @Test
    void tesSecuredEndpoint() throws Exception {
        mockMvc.perform(get("/api/teacher/1"))
                .andExpect(status().is(401));
    }
}