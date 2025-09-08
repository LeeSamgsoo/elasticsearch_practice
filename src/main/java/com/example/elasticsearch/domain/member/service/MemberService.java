package com.example.elasticsearch.domain.member.service;

import com.example.elasticsearch.domain.member.dto.MemberDTO;
import com.example.elasticsearch.domain.member.entity.Member;
import com.example.elasticsearch.domain.member.repository.MemberRepository;
import com.example.elasticsearch.global.jwt.JwtProvider;
import com.example.elasticsearch.global.rsData.RsData;
import com.example.elasticsearch.global.security.SecurityUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public MemberDTO memberJoin(String username, String password) {
        Member checkedMember = this.memberRepository.findByUsername(username).orElse(null);
        if (checkedMember != null) {
            throw new RuntimeException("이미 가입됩 사용자 입니다.");
        }
        Member member = Member.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .build();

        String refreshToken = this.jwtProvider.genRefreshToken(new MemberDTO(member));
        member.toBuilder()
                .refreshToken(refreshToken)
                .build();

        this.memberRepository.save(member);
        return new MemberDTO(member);
    }

    public MemberDTO memberLogin(String username, String password) {
        Member member = this.memberRepository.findByUsername(username).orElse(null);
        if (member == null) {
            throw new RuntimeException("존재하지 않는 사용자 입니다.");
        }
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new RuntimeException("비밀번호가 일치하지 않습니다.");
        }

        return new MemberDTO(member);
    }

    public MemberDTO getMember (String username) {
        Member member = this.memberRepository.findByUsername(username).orElse(null);
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

    public SecurityUser getUserFromAccessToken(String accessToken) {
        Map<String, Object> payloadBody = jwtProvider.getClaims(accessToken);

        long id = (int) payloadBody.get("id");
        String username = (String) payloadBody.get("username");
        List<GrantedAuthority> authorities = new ArrayList<>();

        return new SecurityUser(id, username, "", authorities);
    }
}
