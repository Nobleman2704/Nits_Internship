package com.example.serverapi.config.securiy;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;


    @Value("${jwt.refresh-token.expiration}")
    private Long REFRESH_TOKEN_EXPIRATION;

    @Value("${jwt.access-token.expiration}")
    private Long ACCESS_TOKEN_EXPIRATION;

    public String extractUsername(String token) {
        return extractAllClaim(token, Claims::getSubject);
    }

    public List<SimpleGrantedAuthority> getUserAuthorities(String token) {
        Claims claim = extractAllClaims(token);
        List<String> authorities = (List<String>) claim.get("roles");
        return authorities
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    public <T> T extractAllClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    public String generateAccessToken(UserDetails userDetails) {
        List<String> authorities = getClaims(userDetails.getAuthorities());
        return generateToken(authorities, userDetails, ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(UserDetails userEntity) {
        List<String> authorities = getClaims(userEntity.getAuthorities());

        return generateToken(authorities, userEntity, REFRESH_TOKEN_EXPIRATION);
    }

    private List<String> getClaims(Collection<? extends GrantedAuthority> authorities) {
        return authorities
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
    }


    public String generateToken(
            List<String> authorities,
            UserDetails userDetails,
            Long expirationTime) {
        return Jwts.builder()
                .addClaims(Map.of("roles", authorities))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(getSignInKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractAllClaim(token, Claims::getExpiration);
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
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
