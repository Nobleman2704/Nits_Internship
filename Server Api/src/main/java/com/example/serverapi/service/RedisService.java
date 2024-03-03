package com.example.serverapi.service;

import com.example.serverapi.entity.AuthUser;
import org.springframework.security.core.userdetails.UserDetails;

public interface RedisService {
    void saveUserDetails(String username, AuthUser authUser);

    UserDetails getUserDetailsByUsername(String username);
}
