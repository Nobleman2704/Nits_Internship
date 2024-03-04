package com.example.serverapi.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface RedisService {
    void saveUserDetails(String id, UserDetails authUser);

    UserDetails getUserDetailsById(String id);

    Boolean updateExpirationTime(String id);
}
