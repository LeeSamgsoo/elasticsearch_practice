package com.example.elasticsearch.domain.member.service;

import com.example.elasticsearch.domain.member.dto.MemberDTO;
import com.example.elasticsearch.domain.member.entity.Member;
import com.example.elasticsearch.domain.member.repository.MemberRepository;
import com.example.elasticsearch.global.jwt.JwtProvider;
import com.example.elasticsearch.global.rsData.RsData;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public MemberDTO memberJoin(String username, String password) {
        Member checkedMember = this.memberRepository.findByUsername(username);
        if (checkedMember != null) {
            throw new RuntimeException("이미 가입됩 사용자 입니다.");
        }
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();
        this.memberRepository.save(member);
        return new MemberDTO(member);
    }

    public MemberDTO memberLogin(String username, String password) {
        Member member = this.memberRepository.findByUsername(username);
        if (member == null) {
            throw new RuntimeException("존재하지 않는 사용자 입니다.");
        }

        return new MemberDTO(member);
    }

    public MemberDTO getMember (String username) {
        Member member = this.memberRepository.findByUsername(username);
        if (member == null) {
            throw new RuntimeException("존재하지 않는 사용자 입니다.");
        }

        return new MemberDTO(member);
    }

    public boolean validateToken (String accessToken) {
        return this.jwtProvider.verify(accessToken);
    }

    public RsData<String> refreshAccessToken (String refreshToken) {
        Member member = this.memberRepository.findByRefreshToken(refreshToken).orElseThrow(RuntimeException::new);
        String accessToken = this.jwtProvider.genAccessToken(new MemberDTO(member));
        return RsData.of(
                "200",
                "토큰 갱신 성공",
                accessToken
        );
    }
}
