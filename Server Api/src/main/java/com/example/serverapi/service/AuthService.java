package com.example.serverapi.service;

import com.example.serverapi.dto.auth_user.AuthAndRegRequest;
import com.example.serverapi.dto.token.UserTokenResponse;

public interface AuthService {
    UserTokenResponse authenticate(AuthAndRegRequest authRequest);

    UserTokenResponse register(AuthAndRegRequest regRequest);
}
