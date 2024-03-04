package com.example.serverapi.service.impl;

import com.example.serverapi.dto.auth_user.AuthAndRegRequest;
import com.example.serverapi.dto.product.ResponseDto;
import com.example.serverapi.dto.token.UserTokenResponse;
import com.example.serverapi.entity.AuthUser;
import com.example.serverapi.mapper.AuthUserMapperMapper;
import com.example.serverapi.repository.AuthUserRepository;
import com.example.serverapi.service.AuthService;
import com.example.serverapi.service.JwtService;
import com.example.serverapi.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthUserMapperMapper authUserMapperMapper;
    private final AuthUserRepository authUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final JwtService jwtService;

    @Override
    public ResponseDto<?> register(AuthAndRegRequest regRequest) {
        ResponseDto<?> responseDto = new ResponseDto<>();
        if (authUserRepository.findAuthUserByUsername(regRequest.getUsername()).isPresent()) {
            responseDto.setIsSuccess(false);
            responseDto.setMessage("Username already exists: " + regRequest.getUsername());
            return responseDto;
        }
        AuthUser userEntity = authUserMapperMapper.toEntity(regRequest);

        if (userEntity.getRoleList().isEmpty()) {
            responseDto.setIsSuccess(false);
            responseDto.setMessage("User roles must not be empty");
            return responseDto;
        }

        userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
        authUserRepository.save(userEntity);

        responseDto.setIsSuccess(true);
        responseDto.setMessage("You have successfully been registered");
        return responseDto;
    }

    @Override
    public ResponseDto<UserTokenResponse> authenticate(AuthAndRegRequest authRequest) {
        String username = authRequest.getUsername();

        ResponseDto<UserTokenResponse> responseDto = new ResponseDto<>();

        Optional<AuthUser> optionalAuthUser = authUserRepository.findAuthUserByUsername(username);
        if (optionalAuthUser.isEmpty()) {
            responseDto.setIsSuccess(false);
            responseDto.setMessage("Username or password is incorrect");
            return responseDto;
        }

        AuthUser authUser = optionalAuthUser.get();

        if (!passwordEncoder.matches(authRequest.getPassword(), authUser.getPassword())) {
            responseDto.setIsSuccess(false);
            responseDto.setMessage("Username or password is incorrect");
            return responseDto;
        }

        String uuid = UUID.randomUUID().toString();

        redisService.saveUserDetails(uuid, authUser);

        var accessToken = jwtService.generateAccessToken(uuid);
        var refreshToken = jwtService.generateRefreshToken(uuid);

        responseDto.setIsSuccess(true);
        responseDto.setData(new UserTokenResponse(accessToken, refreshToken));
        return responseDto;
    }

    @Override
    public ResponseDto<UserTokenResponse> getAccessTokenByRefreshToken(String refreshToken) {
        ResponseDto<UserTokenResponse> responseDto = new ResponseDto<>();

        String id;
        try {
            id = jwtService.extractIdFromToken(refreshToken);
        } catch (Exception e) {
            responseDto.setIsSuccess(false);
            responseDto.setMessage("Token is not valid or expired");
            return responseDto;
        }

        redisService.updateExpirationTime(id);

        String accessToken = jwtService.generateAccessToken(id);

        responseDto.setIsSuccess(true);
        responseDto.setData(new UserTokenResponse(accessToken, refreshToken));
        return responseDto;
    }
}
