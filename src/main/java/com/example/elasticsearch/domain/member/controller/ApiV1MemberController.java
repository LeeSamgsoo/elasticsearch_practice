package com.example.elasticsearch.domain.member.controller;

import com.example.elasticsearch.domain.article.request.ArticleCreateRequest;
import com.example.elasticsearch.domain.article.request.ArticleModifyRequest;
import com.example.elasticsearch.domain.article.response.*;
import com.example.elasticsearch.domain.member.dto.MemberDTO;
import com.example.elasticsearch.domain.member.request.MemberRequest;
import com.example.elasticsearch.domain.member.service.MemberService;
import com.example.elasticsearch.global.rsData.RsData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;



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
