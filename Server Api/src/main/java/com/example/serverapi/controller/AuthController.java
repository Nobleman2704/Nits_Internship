package com.example.serverapi.controller;

import com.example.serverapi.dto.auth_user.AuthAndRegRequest;
import com.example.serverapi.dto.product.ResponseDto;
import com.example.serverapi.dto.token.UserTokenResponse;
import com.example.serverapi.service.AuthService;
import com.example.serverapi.service.JwtService;
import com.example.serverapi.util.BindingResultUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Tag(name = "AUTHORIZATION-API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final UserDetailsService userDetailsService;
    private final AuthService authService;
    private final JwtService jwtService;

    @Operation(summary = "It registers user with unique username")
    @PostMapping("/sign-up")
    public ResponseDto<String> register(@Valid
                                        @RequestBody AuthAndRegRequest regRequest,
                                        BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new RuntimeException(BindingResultUtil.extractMessages(bindingResult));

        return ResponseDto.ok(authService.register(regRequest));
    }

    @Operation(summary = "It authenticates user and returns new access and refresh token")
    @PostMapping("/sign-in")
    public ResponseDto<UserTokenResponse> authenticate(@Valid
                                                       @RequestBody AuthAndRegRequest authRequest,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors())
            throw new RuntimeException(BindingResultUtil.extractMessages(bindingResult));

        return ResponseDto.ok(authService.authenticate(authRequest));
    }

    @Operation(summary = "It generates new access token by accepting a refresh token")
    @GetMapping("/refresh_token")
    public ResponseDto<UserTokenResponse> refreshAccessToken(
            HttpServletRequest request) {

        String authentication = request.getHeader(HttpHeaders.AUTHORIZATION);

        String refreshToken = authentication.substring(7);

        String username = jwtService.extractUsername(refreshToken);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String accessToken = jwtService.generateAccessToken(userDetails);

        return ResponseDto.ok(new UserTokenResponse(accessToken, refreshToken));
    }
}
