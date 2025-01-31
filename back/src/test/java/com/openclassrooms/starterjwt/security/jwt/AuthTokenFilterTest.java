package com.openclassrooms.starterjwt.security.jwt;

import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(authTokenFilter, "jwtUtils", jwtUtils);
        ReflectionTestUtils.setField(authTokenFilter, "userDetailsService", userDetailsService);
    }

    @DisplayName("Given a request protected, when it caught with a valid jwt token, then the controller can be asked")
    @Test
    public void testSuccessDoFilterInternal() throws ServletException, IOException {
        // Given
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer validJwtToken");
        Mockito.when(jwtUtils.validateJwtToken("validJwtToken")).thenReturn(true);
        Mockito.when(jwtUtils.getUserNameFromJwtToken("validJwtToken")).thenReturn("username");
        Mockito.when(userDetailsService.loadUserByUsername("username")).thenReturn(userDetails);

        // When
        authTokenFilter.doFilterInternal(request, response, filterChain);
        // Then
        Mockito.verify(filterChain).doFilter(request, response);
        Mockito.verify(userDetailsService).loadUserByUsername("username");
        Mockito.verify(jwtUtils).getUserNameFromJwtToken("validJwtToken");
        Mockito.verify(filterChain).doFilter(request, response);
    }

    @DisplayName("Given a request protected, when it caught with a null jwt token, then the controller can be asked")
    @Test
    public void testNullTokenDoFilterInternal() throws ServletException, IOException {
        // Given
        Mockito.when(request.getHeader("Authorization")).thenReturn(null);
        // When
        authTokenFilter.doFilterInternal(request, response, filterChain);
        // Then
        Mockito.verify(filterChain).doFilter(request, response);
        Mockito.verify(userDetailsService, times(0)).loadUserByUsername("username");
    }

    @DisplayName("Given a request protected, when it caught with a invalid jwt token, then the controller can be asked")
    @Test
    public void testInvalidTokenDoFilterInternal() throws ServletException, IOException {
        // Given
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer invalidJwtToken");
        Mockito.when(jwtUtils.validateJwtToken("invalidJwtToken")).thenReturn(false);
        // When
        authTokenFilter.doFilterInternal(request, response, filterChain);
        // Then
        Mockito.verify(filterChain).doFilter(request, response);
        Mockito.verify(userDetailsService, times(0)).loadUserByUsername(anyString());
    }

    @Test
    public void testExceptionDoFilterInternal() throws ServletException, IOException {
        // Given
        Mockito.when(request.getHeader("Authorization")).thenReturn("Bearer validJwtToken");
        Mockito.when(jwtUtils.validateJwtToken("validJwtToken")).thenThrow(new RuntimeException("exception"));
        // When
        authTokenFilter.doFilterInternal(request, response, filterChain);
        // Then
        Mockito.verify(filterChain).doFilter(request, response);
    }
}