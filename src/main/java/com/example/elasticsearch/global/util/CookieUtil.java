package com.example.elasticsearch.global.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;

import java.util.Arrays;

public class CookieUtil {
    public static String getCookie(HttpServletRequest req, String name) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) return "";
        return Arrays.stream(cookies)
                .filter(c -> c.getName().equals(name))
                .findFirst().map(Cookie::getValue).orElse("");
    }

    public static void addHttpOnlyCookie(HttpServletResponse resp, String name, String value, Long maxAgeSeconds) {
        // SameSite=None; Secure; HttpOnly
        ResponseCookie cookie = ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(maxAgeSeconds)
                .build();
        resp.addHeader("Set-Cookie", cookie.toString());
    }

    public static void clearCookie(HttpServletResponse resp, String name) {
        ResponseCookie cookie = ResponseCookie.from(name, "")
                .httpOnly(true).secure(true).sameSite("None").path("/").maxAge(0).build();
        resp.addHeader("Set-Cookie", cookie.toString());
    }
}
