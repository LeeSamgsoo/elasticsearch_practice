package com.example.elasticsearch.global.jwt;

import com.example.elasticsearch.domain.member.dto.MemberDTO;
import com.example.elasticsearch.global.util.Util;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtProvider {
    private final JwtProperties properties;
    private SecretKey cachedSecretKey;

    public JwtProvider(JwtProperties properties) {
        this.properties = properties;
    }

    // 시크릿키 가지고 오기
    public SecretKey getSecretKey() {
        if (cachedSecretKey == null) {
            cachedSecretKey = Keys.hmacShaKeyFor(properties.getSecretKey().getBytes());
        }

        return cachedSecretKey;
    }

    // 스크릿 키 인코딩
    private SecretKey _getSecretKey() {
        String keyBase64Encoded = Base64.getEncoder().encodeToString(properties.getSecretKey().getBytes());
        return Keys.hmacShaKeyFor(keyBase64Encoded.getBytes());
    }

    // refreshToken 토큰 만들기
    public String genRefreshToken(MemberDTO memberDTO) {
        return genToken(memberDTO, 60 * 60 * 24 * 365 * 1);
    }

    // accessToken 만들기
    public String genAccessToken(MemberDTO memberDTO) {
        return genToken(memberDTO, 60 * 10);
    }


    // 토큰 생성
    public String genToken(MemberDTO memberDTO, int seconds) {
        Map<String, Object> claims = new HashMap<>();

        claims.put("id", memberDTO.getId());
        claims.put("username", memberDTO.getUsername());

        long now = new Date().getTime();
        Date accessTokenExpiresIn = new Date(now + 1000L * seconds);

        return Jwts.builder()
                .claim("body", Util.json.toStr(claims))
                .setExpiration(accessTokenExpiresIn)
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 토큰 유효성 검증
    public boolean verify(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSecretKey())
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    // 클레임 정보 받아오기
    public Map<String, Object> getClaims(String token) {
        String body = Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("body", String.class);

        return Util.toMap(body);
    }

}
