package com.example.serverapi.service;

import com.example.serverapi.entity.AuthUser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
    private static final long TOKEN_EXPIRATION_MINUTES = 1;

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    @Override
    public void saveUserDetails(String username, AuthUser authUser) {
        try {
            String value = objectMapper.writeValueAsString(authUser);
            redisTemplate.opsForValue().set(username, value, TOKEN_EXPIRATION_MINUTES, TimeUnit.MINUTES);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDetails getUserDetailsByUsername(String username) {
        try {
            return objectMapper.readValue(redisTemplate.opsForValue().get(username), UserDetails.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
