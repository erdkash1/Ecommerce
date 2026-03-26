package com.iggy.ecommerce.service;

import com.iggy.ecommerce.dto.AuthResponse;
import com.iggy.ecommerce.dto.LoginRequest;
import com.iggy.ecommerce.dto.RegisterRequest;
import com.iggy.ecommerce.entity.Role;
import com.iggy.ecommerce.entity.User;
import com.iggy.ecommerce.repository.UserRepository;
import com.iggy.ecommerce.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;
    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                      AuthenticationManager authenticationManager, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;

    }

    public AuthResponse register(RegisterRequest request){
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.CUSTOMER);
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail());
        return new AuthResponse(token);

    }

    public AuthResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(), request.getPassword()
                )
        );
        String token = jwtUtil.generateToken(request.getEmail());
        return new AuthResponse(token);
    }
}
