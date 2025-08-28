package com.example.elasticsearch.domain.member.dto.response;

import com.example.elasticsearch.domain.member.dto.MemberDTO;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberResponse {
    private final String username;
    private final LocalDateTime createdDate;

    public MemberResponse(MemberDTO memberDTO) {
        this.username = memberDTO.getUsername();
        this.createdDate = memberDTO.getCreatedDate();
    }
}
