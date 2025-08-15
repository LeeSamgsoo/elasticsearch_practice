package com.example.elasticsearch.domain.member.service;

import com.example.elasticsearch.domain.member.dto.MemberDTO;
import com.example.elasticsearch.domain.member.entity.Member;
import com.example.elasticsearch.domain.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
}
