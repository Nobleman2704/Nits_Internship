package com.example.serverapi.service.impl;

import com.example.serverapi.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    @Value("${redis.expiration}")
    private Long TOKEN_EXPIRATION_SECONDS;

    //    private final StringRedisTemplate redisTemplate;

    private final RedisTemplate<String, Object> redisTemplateAuth;

    @Override
    public void saveUserDetails(String id, UserDetails authUser) {
        redisTemplateAuth.opsForValue().set(id, authUser,
                TOKEN_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }

    @Override
    public UserDetails getUserDetailsById(String id) {
        return (UserDetails) redisTemplateAuth.opsForValue().get(id);
    }

    @Override
    public Boolean updateExpirationTime(String id) {
        return redisTemplateAuth.expire(id, TOKEN_EXPIRATION_SECONDS, TimeUnit.SECONDS);
    }
}
