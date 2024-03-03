package com.example.serverapi.service.impl;

import com.example.serverapi.dto.auth_user.AuthAndRegRequest;
import com.example.serverapi.dto.token.UserTokenResponse;
import com.example.serverapi.entity.AuthUser;
import com.example.serverapi.mapper.AuthUserMapperMapper;
import com.example.serverapi.repository.AuthUserRepository;
import com.example.serverapi.service.AuthService;
import com.example.serverapi.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthUserMapperMapper authUserMapperMapper;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public String register(AuthAndRegRequest regRequest) {
        if (authUserRepository.findAuthUserByUsername(regRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists: " + regRequest.getUsername());
        }
        AuthUser userEntity = authUserMapperMapper.toEntity(regRequest);

        if (userEntity.getRoleList().isEmpty())
            throw new RuntimeException("User roles must not be empty");

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        authUserRepository.save(userEntity);

        return "You have successfully been registered";
    }

    @Override
    public UserTokenResponse authenticate(AuthAndRegRequest authRequest) {
        String username = authRequest.getUsername();

        AuthUser userEntity = authUserRepository.findAuthUserByUsername(username)
                .orElseThrow(() -> new RuntimeException("Username or password is incorrect"));

        if (!passwordEncoder.matches(authRequest.getPassword(), userEntity.getPassword()))
            throw new RuntimeException("Username or password is incorrect");

        var accessToken = jwtService.generateAccessToken(userEntity);
        var refreshToken = jwtService.generateRefreshToken(userEntity);
        return new UserTokenResponse(accessToken, refreshToken);
    }
}
