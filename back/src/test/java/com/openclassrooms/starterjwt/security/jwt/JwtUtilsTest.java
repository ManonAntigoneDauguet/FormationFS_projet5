package com.openclassrooms.starterjwt.security;

import com.openclassrooms.starterjwt.security.jwt.JwtUtils;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

@TestPropertySource(properties = "oc.app.jwtSecret=test_openclassrooms")  // Définition de la propriété pour le test
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtUtilsTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    String validToken;

    @BeforeEach
    void setUp() {
        validToken = Jwts.builder()
                .setSubject("yoga@studio.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(Long.MAX_VALUE)) // Expiration infinie pour les tests
                .signWith(SignatureAlgorithm.HS512, "test_openclassrooms")
                .compact();
    }

    @DisplayName("Given a token X created from a email, when getUserNameFromJwtToken(X), is called, then the user email is returned")
    @Test
    void testGetUserNameFromJwtToken() {
        String email = jwtUtils.getUserNameFromJwtToken(validToken);
        assertEquals("yoga@studio.com", email);
    }

    @DisplayName("Given a valid token X, when validateJwtToken(X), is called, then it returns true")
    @Test
    void testSuccessValidateJwtToken() {
        // Given
        JwtParser jwtParser = mock(JwtParser.class);
        Mockito.when(jwtParser.setSigningKey((byte[]) any())).thenReturn(jwtParser);
        Mockito.when(jwtParser.parseClaimsJws(validToken)).thenReturn(mock(Jws.class));
        // When
        boolean result = jwtUtils.validateJwtToken(validToken);
        // Then
        assertTrue(result);
    }

    @DisplayName("Given a token X with bad format, when validateJwtToken(X), is called, then an exception is thrown")
    @Test
    void testMalformedJwtExceptionValidateJwtToken() {
        // Given
        String invalidToken = "Token!";
        JwtParser jwtParser = mock(JwtParser.class);
        Mockito.when(jwtParser.setSigningKey((byte[]) any())).thenReturn(jwtParser);
        Mockito.when(jwtParser.parseClaimsJws(invalidToken)).thenReturn(mock(Jws.class));
        // When
        boolean result = jwtUtils.validateJwtToken(invalidToken);
        // Then
        assertFalse(result);
    }

    @DisplayName("Given a token X with the incorrect signature, when validateJwtToken(X), is called, then an exception is thrown")
    @Test
    void testSignatureExceptionValidateJwtToken() {
        // Given
        String invalidToken = Jwts.builder()
                .setSubject("yoga@studio.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(Long.MAX_VALUE)) // Expiration infinie pour les tests
                .signWith(SignatureAlgorithm.HS512, "bad_key")
                .compact();
        JwtParser jwtParser = mock(JwtParser.class);
        Mockito.when(jwtParser.setSigningKey((byte[]) any())).thenReturn(jwtParser);
        Mockito.when(jwtParser.parseClaimsJws(invalidToken)).thenReturn(mock(Jws.class));
        // When
        boolean result = jwtUtils.validateJwtToken(invalidToken);
        // Then
        assertFalse(result);
    }

    @DisplayName("Given a expired token X, when validateJwtToken(X), is called, then an exception is thrown")
    @Test
    void testExpiredJwtExceptionValidateJwtToken() {
        // Given
        String invalidToken = Jwts.builder()
                .setSubject("yoga@studio.com")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() - 1000)) // Expiration infinie pour les tests
                .signWith(SignatureAlgorithm.HS512, "test_openclassrooms")
                .compact();
        JwtParser jwtParser = mock(JwtParser.class);
        Mockito.when(jwtParser.setSigningKey((byte[]) any())).thenReturn(jwtParser);
        Mockito.when(jwtParser.parseClaimsJws(invalidToken)).thenReturn(mock(Jws.class));
        // When
        boolean result = jwtUtils.validateJwtToken(invalidToken);
        // Then
        assertFalse(result);
    }

    @DisplayName("Given a empty token X, when validateJwtToken(X), is called, then an exception is thrown")
    @Test
    void testIllegalArgumentExceptionValidateJwtToken() {
        // Given
        String invalidToken = "";
        JwtParser jwtParser = mock(JwtParser.class);
        Mockito.when(jwtParser.setSigningKey((byte[]) any())).thenReturn(jwtParser);
        Mockito.when(jwtParser.parseClaimsJws(invalidToken)).thenReturn(mock(Jws.class));
        // When
        boolean result = jwtUtils.validateJwtToken(invalidToken);
        // Then
        assertFalse(result);
    }
}