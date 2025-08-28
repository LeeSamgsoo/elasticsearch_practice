package com.example.elasticsearch.domain.member.controller;

import com.example.elasticsearch.domain.member.dto.MemberDTO;
import com.example.elasticsearch.domain.member.dto.request.MemberRequest;
import com.example.elasticsearch.domain.member.dto.response.MemberResponse;
import com.example.elasticsearch.domain.member.service.MemberService;
import com.example.elasticsearch.global.jwt.JwtProvider;
import com.example.elasticsearch.global.rsData.RsData;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


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
    public RsData<?> memberLogin (@Valid @RequestBody MemberRequest memberRequest) {
        MemberDTO memberDTO = this.memberService.memberLogin(
                memberRequest.getUsername(),
                memberRequest.getPassword()
        );
        String token = this.jwtProvider.genAccessToken(memberDTO);
        return RsData.of(
                "200",
                "로그인 성공",
                token
        );
    }
}
