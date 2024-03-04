package com.example.serverapi.service;

public interface JwtService {
    String extractIdFromToken(String token);

//    List<SimpleGrantedAuthority> getUserAuthorities(String token);

    String generateAccessToken(String uuid);

    String generateRefreshToken(String uuid);
}
