package com.example.elasticsearch.global.security;

import com.example.elasticsearch.global.jwt.JwtProperties;
import com.example.elasticsearch.global.jwt.JwtService;
import com.example.elasticsearch.global.util.CookieUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProperties props;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws ServletException, IOException {

        String uri = req.getRequestURI();
        // 로그인/회원가입 등 화이트리스트는 통과
        if (uri.startsWith("/api/v1/auth/login") || uri.startsWith("/api/v1/auth/join")) {
            chain.doFilter(req, resp);
            return;
        }

        String access = CookieUtil.getCookie(req, "accessToken");
        if (!access.isBlank() && jwtService.isValid(access)) {
            Authentication auth = jwtService.toAuthentication(access);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            // 선택: refreshToken로 재발급
            String refresh = CookieUtil.getCookie(req, "refreshToken");
            if (!refresh.isBlank() && jwtService.isValid(refresh)) {
                Claims c = jwtService.parse(refresh);
                String username = c.get("username", String.class);

                // 권한은 최소 기본권한 부여 또는 DB 조회 후 반영
                List<GrantedAuthority> auths = List.of(new SimpleGrantedAuthority("ROLE_USER"));
                String newAccess = jwtService.createAccessToken(username, auths);
                CookieUtil.addHttpOnlyCookie(resp, "accessToken", newAccess,  props.getAccessExpSeconds());

                Authentication auth = jwtService.toAuthentication(newAccess);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        chain.doFilter(req, resp);
    }
}
