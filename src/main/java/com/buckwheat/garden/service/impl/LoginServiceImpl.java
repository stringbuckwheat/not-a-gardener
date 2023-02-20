package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.repository.MemberRepository;
import com.buckwheat.garden.service.JwtAuthTokenProvider;
import com.buckwheat.garden.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
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
    private final AuthenticationManager passwordAuthAuthenticationManager;
    private final JwtAuthTokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;


    @Override
    public String login(MemberDto memberDto) {
        log.debug("parameter check: {}", memberDto);

        Member member = memberRepository.findByUsername(memberDto.getUsername())
                .orElseThrow(() -> new BadCredentialsException("아이디 오류"));

        if(!encoder.matches(memberDto.getPw(), member.getPw())){
            log.debug("비밀번호 오류");
            throw new BadCredentialsException("비밀번호 오류");
        }

        UserPrincipal userPrincipal = UserPrincipal.create(member);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.singleton(new SimpleGrantedAuthority("USER")));
        SecurityContextHolder.getContext().setAuthentication(token);

        return tokenProvider.createAuthToken(userPrincipal).getToken();
    }
}
