package com.example.serverapi.dto.token;

import lombok.*;

@Data
@AllArgsConstructor
public class UserTokenResponse {
    private String accessToken;
    private String refreshToken;
}
