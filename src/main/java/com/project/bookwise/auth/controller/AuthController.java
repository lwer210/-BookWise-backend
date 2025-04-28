package com.project.bookwise.auth.controller;

import com.project.bookwise.auth.service.AuthService;
import com.project.bookwise.common.jwt.JwtDto;
import com.project.bookwise.user.domain.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user){
        User result = authService.register(user);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user){
        return ResponseEntity.ok(authService.login(user));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody JwtDto jwtDto){
        authService.refresh(jwtDto);
    }
}
