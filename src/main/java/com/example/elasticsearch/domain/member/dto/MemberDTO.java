package com.example.elasticsearch.domain.member.dto;

import com.example.elasticsearch.domain.member.entity.Member;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MemberDTO {
    private final Long id;
    private final String username;
    private final LocalDateTime createdDate;
    private final LocalDateTime modifiedDate;

    public MemberDTO(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.createdDate = member.getCreatedDate();
        this.modifiedDate = member.getModifiedDate();
    }
}
