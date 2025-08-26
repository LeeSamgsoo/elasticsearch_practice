package com.example.elasticsearch.domain.member.controller;

import com.example.elasticsearch.domain.member.dto.request.MemberRequest;
import com.example.elasticsearch.domain.member.service.MemberService;
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

    @PostMapping("/join")
    public String memberJoin (@Valid @RequestBody MemberRequest memberRequest) {
        this.memberService.memberJoin(
                memberRequest.getUsername(),
                memberRequest.getPassword()
        );
        return "가입완료";
    }
}
