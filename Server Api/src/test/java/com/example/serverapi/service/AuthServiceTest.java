package com.example.serverapi.service;

import com.example.serverapi.dto.auth_user.AuthAndRegRequest;
import com.example.serverapi.dto.product.ResponseDto;
import com.example.serverapi.dto.token.UserTokenResponse;
import com.example.serverapi.enums.Authority;
import com.example.serverapi.enums.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthServiceTest {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private RedisService redisService;

    private AuthAndRegRequest authRequest;
    private AuthAndRegRequest regRequest;

    @BeforeEach
    public void setUp(){
        authRequest = new AuthAndRegRequest();
        authRequest.setUsername("Asadbek");
        authRequest.setPassword("Asadbek@2704");

        regRequest = new AuthAndRegRequest();
        regRequest.setUsername("Asadbek");
        regRequest.setPassword("Asadbek@2704");
        regRequest.setRoleList(List.of(Role.USER));
        regRequest.setAuthorityList(List.of(Authority.READ, Authority.CREATE));
    }

    @Test
    void register() {
        ResponseDto<?> responseDto = authService.register(regRequest);

        assertThat(responseDto.getIsSuccess()).isEqualTo(true);
    }

    @Test
    void authenticate() {
        authService.register(regRequest);

        ResponseDto<UserTokenResponse> responseDto = authService.authenticate(authRequest);

        UserTokenResponse userTokenResponse = responseDto.getData();

        String accessToken = userTokenResponse.getAccessToken();

        String id = jwtService.extractIdFromToken(accessToken);

        UserDetails userDetails = redisService.getUserDetailsById(id);

        assertThat(userDetails.getUsername()).isEqualTo(authRequest.getUsername());
    }

    @Test
    void getAccessTokenByRefreshToken() {

        authService.register(regRequest);

        ResponseDto<UserTokenResponse> responseDto = authService.authenticate(authRequest);
        UserTokenResponse userTokenResponse = responseDto.getData();

        ResponseDto<UserTokenResponse> responseDto1 = authService
                .getAccessTokenByRefreshToken(userTokenResponse.getRefreshToken());

        UserTokenResponse userTokenResponse1 = responseDto1.getData();

        String accessToken = userTokenResponse1.getAccessToken();

        String id = jwtService.extractIdFromToken(accessToken);

        UserDetails userDetails = redisService.getUserDetailsById(id);

        assertThat(userDetails.getUsername()).isEqualTo(authRequest.getUsername());
    }
}