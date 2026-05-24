package com.iggy.ecommerce;

import com.iggy.ecommerce.dto.RegisterRequest;
import com.iggy.ecommerce.dto.AuthResponse;
import com.iggy.ecommerce.entity.User;
import com.iggy.ecommerce.repository.UserRepository;
import com.iggy.ecommerce.security.JwtUtil;
import com.iggy.ecommerce.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserSuccessfully() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("test@gmail.com");
        request.setPassword("password123");

        User savedUser = new User();
        savedUser.setName("Test User");
        savedUser.setEmail("test@gmail.com");
        savedUser.setPassword("encodedPassword");

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(jwtUtil.generateToken(anyString())).thenReturn("mockToken");

        // When
        AuthResponse response = authService.register(request);

        // Then
        assertNotNull(response);
        assertEquals("mockToken", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldFailWhenEmailAlreadyExists() {
        // Given
        RegisterRequest request = new RegisterRequest();
        request.setName("Test User");
        request.setEmail("existing@gmail.com");
        request.setPassword("password123");

        User existingUser = new User();
        existingUser.setEmail("existing@gmail.com");

        when(userRepository.findByEmail("existing@gmail.com"))
                .thenReturn(Optional.of(existingUser));

        // When & Then
        assertThrows(RuntimeException.class,
                () -> authService.register(request));

        verify(userRepository, never()).save(any(User.class));
    }
}