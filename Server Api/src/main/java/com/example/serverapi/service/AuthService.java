package com.example.serverapi.service;

import com.example.serverapi.dto.auth_user.AuthAndRegRequest;
import com.example.serverapi.dto.product.ResponseDto;
import com.example.serverapi.dto.token.UserTokenResponse;

public interface AuthService {
    ResponseDto<UserTokenResponse> authenticate(AuthAndRegRequest authRequest);

    ResponseDto<?> register(AuthAndRegRequest regRequest);

    ResponseDto<UserTokenResponse> getAccessTokenByRefreshToken(String refreshToken);
}
