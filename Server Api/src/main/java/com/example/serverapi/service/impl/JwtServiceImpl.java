package com.example.serverapi.service.impl;

import com.example.serverapi.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {
    @Value("${jwt.secret-key}")
    private String secret_key;

    @Value("${jwt.expiration.refresh-token}")
    private Long REFRESH_TOKEN_EXPIRATION;

    @Value("${jwt.expiration.access-token}")
    private Long ACCESS_TOKEN_EXPIRATION;

    @Override
    public String extractIdFromToken(String token) {
        return extractAllClaim(token, Claims::getSubject);
    }

    @Override
    public String generateAccessToken(String uuid) {
        return generateToken(uuid, ACCESS_TOKEN_EXPIRATION);
    }

    @Override
    public String generateRefreshToken(String uuid) {
        return generateToken(uuid, REFRESH_TOKEN_EXPIRATION);
    }

    private String generateToken(String uuid, Long expirationTime) {
        return Jwts.builder()
                .setSubject(uuid)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + (expirationTime * 1000)))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret_key);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private <T> T extractAllClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }
}
