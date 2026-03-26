package com.iggy.ecommerce.controller;

import com.iggy.ecommerce.dto.AuthResponse;
import com.iggy.ecommerce.dto.LoginRequest;
import com.iggy.ecommerce.dto.RegisterRequest;
import com.iggy.ecommerce.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private AuthService authService;
    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    @PostMapping("/register")
    public AuthResponse createRegister(@RequestBody RegisterRequest request){ return authService.register(request);}

    @PostMapping("/login")
    public AuthResponse loginRequest(@RequestBody LoginRequest request){ return authService.login(request);}
}
