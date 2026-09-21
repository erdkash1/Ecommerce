package com.iggy.ecommerce.service;

import com.iggy.ecommerce.dto.AuthResponse;
import com.iggy.ecommerce.dto.LoginRequest;
import com.iggy.ecommerce.dto.RegisterRequest;
import com.iggy.ecommerce.entity.Role;
import com.iggy.ecommerce.entity.User;
import com.iggy.ecommerce.repository.UserRepository;
import com.iggy.ecommerce.security.JwtUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    private RegisterRequest registerRequest;
    private LoginRequest loginRequest;

    @BeforeEach
    void setUp() {
        registerRequest = new RegisterRequest();
        registerRequest.setName("Iggy Shirmen");
        registerRequest.setEmail("iggy@gmail.com");
        registerRequest.setPassword("password123");

        loginRequest = new LoginRequest();
        loginRequest.setEmail("iggy@gmail.com");
        loginRequest.setPassword("password123");
    }


    @Test
    void shouldRegisterUserAndReturnToken() {
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(jwtUtil.generateToken("iggy@gmail.com")).thenReturn("mock.jwt.token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
        verify(passwordEncoder, times(1)).encode("password123");
        verify(jwtUtil, times(1)).generateToken("iggy@gmail.com");
    }

    @Test
    void shouldEncodePasswordBeforeSaving() {
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");
        when(jwtUtil.generateToken(any())).thenReturn("token");

        authService.register(registerRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("hashedPassword", savedUser.getPassword());
        assertNotEquals("password123", savedUser.getPassword());
    }

    @Test
    void shouldAssignCustomerRoleOnRegister() {
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(jwtUtil.generateToken(any())).thenReturn("token");

        authService.register(registerRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals(Role.CUSTOMER, savedUser.getRole());
    }

    @Test
    void shouldSaveCorrectEmailOnRegister() {
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(jwtUtil.generateToken(any())).thenReturn("token");

        authService.register(registerRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertEquals("iggy@gmail.com", savedUser.getEmail());
        assertEquals("Iggy Shirmen", savedUser.getName());
    }

    @Test
    void shouldReturnNonNullTokenOnRegister() {
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(jwtUtil.generateToken(any())).thenReturn("valid.jwt.token");

        AuthResponse response = authService.register(registerRequest);

        assertNotNull(response.getToken());
        assertFalse(response.getToken().isEmpty());
    }


    @Test
    void shouldLoginAndReturnToken() {
        when(jwtUtil.generateToken("iggy@gmail.com")).thenReturn("mock.jwt.token");

        AuthResponse response = authService.login(loginRequest);

        assertNotNull(response);
        assertEquals("mock.jwt.token", response.getToken());
        verify(authenticationManager, times(1)).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );
        verify(jwtUtil, times(1)).generateToken("iggy@gmail.com");
    }

    @Test
    void shouldCallAuthenticationManagerOnLogin() {
        when(jwtUtil.generateToken(any())).thenReturn("token");

        authService.login(loginRequest);

        verify(authenticationManager, times(1)).authenticate(
                any(UsernamePasswordAuthenticationToken.class)
        );
    }

    @Test
    void shouldThrowExceptionForInvalidCredentials() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        assertThrows(BadCredentialsException.class, () ->
                authService.login(loginRequest)
        );

        verify(jwtUtil, never()).generateToken(any());
    }

    @Test
    void shouldNotGenerateTokenWhenAuthFails() {
        when(authenticationManager.authenticate(any()))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () ->
                authService.login(loginRequest)
        );

        verify(jwtUtil, never()).generateToken(any());
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldGenerateTokenWithCorrectEmail() {
        when(jwtUtil.generateToken("iggy@gmail.com")).thenReturn("token.for.iggy");

        AuthResponse response = authService.login(loginRequest);

        assertEquals("token.for.iggy", response.getToken());
        verify(jwtUtil).generateToken("iggy@gmail.com");
    }
}