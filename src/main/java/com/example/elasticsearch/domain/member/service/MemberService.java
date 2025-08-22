package com.example.elasticsearch.domain.member.service;

import com.example.elasticsearch.domain.member.entity.Member;
import com.example.elasticsearch.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public void memberJoin(String username, String password) {
        Member member = Member.builder()
                .username(username)
                .password(password)
                .build();
        this.memberRepository.save(member);
    }
}
