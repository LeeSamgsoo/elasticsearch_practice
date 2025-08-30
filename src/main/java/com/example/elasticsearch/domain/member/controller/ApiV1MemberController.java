package com.example.elasticsearch.domain.member.controller;

import com.example.elasticsearch.domain.member.dto.MemberDTO;
import com.example.elasticsearch.domain.member.dto.request.MemberRequest;
import com.example.elasticsearch.domain.member.dto.response.MemberResponse;
import com.example.elasticsearch.domain.member.service.MemberService;
import com.example.elasticsearch.global.jwt.JwtProvider;
import com.example.elasticsearch.global.rsData.RsData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1/members")
@Tag(name = "ApiV1MemberController", description = "회원 인증 인가 API")
public class ApiV1MemberController {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @PostMapping("/join")
    public RsData<MemberResponse> memberJoin (@Valid @RequestBody MemberRequest memberRequest) {
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
    public RsData<MemberResponse> memberLogin (@Valid @RequestBody MemberRequest memberRequest,
                                               HttpServletResponse res) {
        MemberDTO memberDTO = this.memberService.memberLogin(
                memberRequest.getUsername(),
                memberRequest.getPassword()
        );

        String accessToken = this.jwtProvider.genAccessToken(memberDTO);
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 60);
        res.addCookie(cookie);

        return RsData.of(
                "200",
                "로그인 성공" + accessToken,
                new MemberResponse(memberDTO)
        );
    }

    @GetMapping("/me")
    public RsData<MemberResponse> memberGetMyInfo (HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        String accessToken = "";
        for (Cookie cookie : cookies) {
            if ("accessToken".equals(cookie.getName())) {
                accessToken = cookie.getValue();
            }
        }

        Map<String, Object> claims = this.jwtProvider.getClaims(accessToken);
        String username = (String) claims.get("username");
        MemberDTO memberDTO = this.memberService.getMember(username);
        return RsData.of(
                "200",
                "내 회원정보",
                new MemberResponse(memberDTO)
        );
    }
}
