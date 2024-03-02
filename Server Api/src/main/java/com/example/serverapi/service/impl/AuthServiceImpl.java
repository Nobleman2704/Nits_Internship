package com.example.serverapi.service.impl;

import com.example.serverapi.config.securiy.JwtService;
import com.example.serverapi.dto.auth_user.AuthAndRegRequest;
import com.example.serverapi.dto.token.UserTokenResponse;
import com.example.serverapi.entity.AuthUser;
import com.example.serverapi.enums.Role;
import com.example.serverapi.mapper.AuthUserMapperMapper;
import com.example.serverapi.repository.AuthUserRepository;
import com.example.serverapi.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthUserMapperMapper authUserMapperMapper;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserTokenResponse register(AuthAndRegRequest regRequest) {
        if (authUserRepository.findUserEntityByUsername(regRequest.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists: " + regRequest.getUsername());
        }
        AuthUser userEntity = authUserMapperMapper.toEntity(regRequest);

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        userEntity.setRoleList(List.of(Role.USER));
        authUserRepository.save(userEntity);

        String accessToken = jwtService.generateAccessToken(userEntity);
        String refreshToken = jwtService.generateRefreshToken(userEntity);
        return new UserTokenResponse(accessToken, refreshToken);
    }

    @Override
    public UserTokenResponse authenticate(AuthAndRegRequest authRequest) {
//        authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(
//                        authRequest.getUsername(),
//                        authRequest.getPassword()
//                )
//        );

        AuthUser userEntity = authUserRepository.findUserEntityByUsername(authRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("Username or password is incorrect"));

        var accessToken = jwtService.generateAccessToken(userEntity);
        var refreshToken = jwtService.generateRefreshToken(userEntity);
        return new UserTokenResponse(accessToken, refreshToken);
    }
}
