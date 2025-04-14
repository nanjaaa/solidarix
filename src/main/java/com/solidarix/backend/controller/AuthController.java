package com.solidarix.backend.controller;

import com.solidarix.backend.dto.AuthRequestDto;
import com.solidarix.backend.dto.AuthResponseDto;
import com.solidarix.backend.dto.RegistrationDto;
import com.solidarix.backend.service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public AuthResponseDto register(@RequestBody RegistrationDto request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponseDto authenticate(@RequestBody AuthRequestDto request){
        return authService.authenticate(request);
    }

}
