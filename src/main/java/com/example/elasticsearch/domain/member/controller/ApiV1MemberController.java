package com.example.elasticsearch.domain.member.controller;

import com.example.elasticsearch.domain.member.dto.MemberDTO;
import com.example.elasticsearch.domain.member.dto.request.MemberRequest;
import com.example.elasticsearch.domain.member.dto.response.MemberResponse;
import com.example.elasticsearch.domain.member.service.MemberService;
import com.example.elasticsearch.global.jwt.JwtProperties;
import com.example.elasticsearch.global.jwt.JwtService;
import com.example.elasticsearch.global.rsData.RsData;
import com.example.elasticsearch.global.util.CookieUtil;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/members")
@Tag(name = "ApiV1MemberController", description = "회원 인증 인가 API")
public class ApiV1MemberController {
    private final MemberService memberService;
    private final JwtService jwtService;
    private final JwtProperties props;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/join")
    public RsData<MemberResponse> memberJoin(@Valid @RequestBody MemberRequest memberRequest) {
        MemberDTO memberDTO = this.memberService.memberJoin(
                memberRequest.getUsername(),
                memberRequest.getPassword()
        );
        return RsData.of(
                "200",
                "회원 가입 완료",
                new MemberResponse(memberDTO)
        );
    }

    @PostMapping("/login")
    public RsData<MemberResponse> memberLogin(@Valid @RequestBody MemberRequest memberRequest,
                                              HttpServletResponse res) {
        Authentication auth = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        memberRequest.getUsername(),
                        memberRequest.getPassword()
                )
        );

        MemberDTO memberDTO = this.memberService.memberLogin(
                memberRequest.getUsername(),
                memberRequest.getPassword()
        );

        Collection<? extends GrantedAuthority> auths = auth.getAuthorities();

        String accessToken  = jwtService.createAccessToken(memberDTO.getUsername(), auths);
        String refreshToken = jwtService.createRefreshToken(memberDTO.getUsername());

        CookieUtil.addHttpOnlyCookie(res, "accessToken", accessToken,  props.getAccessExpSeconds());
        CookieUtil.addHttpOnlyCookie(res, "refreshToken", refreshToken, props.getRefreshExpSeconds());

        return RsData.of(
                "200",
                "로그인 성공" + accessToken,
                new MemberResponse(memberDTO)
        );
    }

    @GetMapping("/me")
    public RsData<MemberResponse> memberGetMyInfo(HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        String accessToken = "";
        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) {
                accessToken = cookie.getValue();
            }
        }

        Map<String, Object> claims = this.jwtService.parse(accessToken);
        String username = (String) claims.get("username");
        MemberDTO memberDTO = this.memberService.getMember(username);
        return RsData.of(
                "200",
                "내 회원정보",
                new MemberResponse(memberDTO)
        );
    }

    @GetMapping("/logout")
    public RsData<?> memberLogout(HttpServletResponse res) {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        res.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        res.addCookie(refreshTokenCookie);

        return RsData.of(
                "200",
                "로그아웃 성공"
        );
    }
}
