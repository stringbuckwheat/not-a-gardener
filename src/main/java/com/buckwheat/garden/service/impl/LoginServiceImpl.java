package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.dto.MemberInfo;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.repository.MemberRepository;
import com.buckwheat.garden.service.JwtAuthTokenProvider;
import com.buckwheat.garden.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final JwtAuthTokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    /**
     * MemberDto의 ID, PW를 사용하여 DB를 통한 인증
     * 인증 성공 시 그 과정에서 받아온 Member 객체를 사용해 UserPrincipal을 만들고,
     * UsernamePasswordAuthenticationToken를 Security Context에 저장
     * 이후 JwtAuthToken을 생성 후 리턴한다.
     *
     * @param memberDto
     * @return JwtAuthToken을 인코딩한 String 값 리턴
     */
    @Override
    public MemberInfo login(MemberDto memberDto) {
        log.debug("parameter check: {}", memberDto);

        Member member = memberRepository.findByUsername(memberDto.getUsername())
                .orElseThrow(() -> new BadCredentialsException("아이디 오류"));

        // 비밀번호 일치 여부 검사
        if(!encoder.matches(memberDto.getPw(), member.getPw())){
            log.debug("비밀번호 오류");
            throw new BadCredentialsException("비밀번호 오류");
        }

        // 인증 성공
        // member 객체를 포함한 userPrincipal 생성
        UserPrincipal userPrincipal = UserPrincipal.create(member);

        // Authentication에 담을 토큰 생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.singleton(new SimpleGrantedAuthority("USER")));

        // security context에 저장
        SecurityContextHolder.getContext().setAuthentication(token);

        // 인코딩된 값 리턴
        String jwtToken = tokenProvider.createAuthToken(userPrincipal).getToken();

        return new MemberInfo(jwtToken, member.getMemberNo(), member.getName());
    }
}
