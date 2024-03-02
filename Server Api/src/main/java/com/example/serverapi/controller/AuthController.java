package com.example.serverapi.controller;

import com.example.serverapi.config.securiy.JwtService;
import com.example.serverapi.dto.auth_user.AuthAndRegRequest;
import com.example.serverapi.dto.token.UserTokenResponse;
import com.example.serverapi.service.AuthService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AUTHORIZATION-API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;


    @PostMapping("/register")
    public ResponseEntity<UserTokenResponse> register(
            @RequestBody AuthAndRegRequest regRequest) {

        return ResponseEntity.ok(authService.register(regRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<UserTokenResponse> authenticate(
            @RequestBody AuthAndRegRequest authRequest) {

        return ResponseEntity.ok(authService.authenticate(authRequest));
    }

    @GetMapping("/refresh_token")
    public ResponseEntity<UserTokenResponse> refreshAccessToken(
            HttpServletRequest request) {
        String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);

        String token = authentication.substring(7);

        String username = jwtService.extractUsername(token);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String accessToken = jwtService.generateAccessToken(userDetails);
        String refreshToken = jwtService.generateRefreshToken(userDetails);

        return ResponseEntity.ok(new UserTokenResponse(accessToken, refreshToken));
    }
}
