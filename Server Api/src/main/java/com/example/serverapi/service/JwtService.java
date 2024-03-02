package com.example.serverapi.service;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

public interface JwtService {
    String extractUsername(String token);

    List<SimpleGrantedAuthority> getUserAuthorities(String token);

    String generateAccessToken(UserDetails userDetails);

    String generateRefreshToken(UserDetails userEntity);
}
