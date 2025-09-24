package com.example.elasticsearch.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties props;
    private SecretKey cachedKey;

    private SecretKey key() {
        if (cachedKey == null) {
            // 비밀키는 충분히 길어야 함(HS512 권장 64바이트 이상)
            cachedKey = io.jsonwebtoken.security.Keys.hmacShaKeyFor(props.getSecretKey().getBytes());
        }
        return cachedKey;
    }

    public String createAccessToken(String username, Collection<? extends GrantedAuthority> auths) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("roles", auths.stream().map(GrantedAuthority::getAuthority).toArray(String[]::new));
        Date exp = new Date(System.currentTimeMillis() + 1000L * props.getAccessExpSeconds());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken(String username) {
        Map<String, Object> claims = Map.of("username", username);
        Date exp = new Date(System.currentTimeMillis() + 1000L * props.getRefreshExpSeconds());
        return Jwts.builder()
                .setClaims(claims)
                .setExpiration(exp)
                .signWith(key(), SignatureAlgorithm.HS512)
                .compact();
    }

    public boolean isValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
    }

    /**
     * 토큰 → Authentication (SecurityContext에 넣을 객체)
     */
    public Authentication toAuthentication(String token) {
        Claims c = parse(token);
        String username = c.get("username", String.class);
        List<SimpleGrantedAuthority> auths = Optional.ofNullable((List<String>) c.get("roles"))
                .orElseGet(List::of)
                .stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        // 비밀번호는 필요 없음(이미 토큰 검증으로 인증됨)
        UserDetails principal = org.springframework.security.core.userdetails.User
                .withUsername(username).password("").authorities(auths).build();

        return new UsernamePasswordAuthenticationToken(principal, token, auths);
    }
}
