package com.example.elasticsearch.global.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApiSecurityConfig {
    @Bean
    SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .authorizeHttpRequests(
                        authorizeRequests -> authorizeRequests
                                .requestMatchers(HttpMethod.GET, "/api/*/articles").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/*/articles/*").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/members/join").permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/*/members/login").permitAll() // 로그인은 누구나 가능, post 요청만 허용
                                .requestMatchers(HttpMethod.GET, "/api/*/members/me").permitAll() // 임시
                                .anyRequest().authenticated()
                )
                .csrf(
                        AbstractHttpConfigurer::disable
                ) // csrf 토큰 끄기
                .httpBasic(
                        AbstractHttpConfigurer::disable
                ) // httpBasic 로그인 방식 끄기
                .formLogin(
                        AbstractHttpConfigurer::disable
                ) // 폼 로그인 방식 끄기
                .sessionManagement(
                        sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        ;
        return http.build();
    }
}
