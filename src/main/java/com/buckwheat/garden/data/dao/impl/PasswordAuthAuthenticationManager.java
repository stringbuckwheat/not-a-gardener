package com.buckwheat.garden.data.dao.impl;

import com.buckwheat.garden.data.dto.PasswordAuthenticationToken;
import com.buckwheat.garden.data.entity.MemberAuth;
import com.buckwheat.garden.repository.MemberAuthRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PasswordAuthAuthenticationManager implements AuthenticationProvider {
    @Autowired
    private final MemberAuthRepository memberAuthRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        /*
        상속도
        Authentication, CredentialsContainer
        ↑
        AbstractAuthenticationToken
        ↑
        UsernamePasswordAuthenticationToken
        ↑
        PasswordAuthenticationToken (내가 만듦)

        => 따라서 Authentication 타입 변수에 담을 수 있다.
        */

        log.debug("PasswordAuthAuthenticationManager.authenticate() 호출");

        // PasswordAuthenticationToken의 principal == user의 id
        // Optional<MemberAuth> memberAuth

        MemberAuth memberAuth = memberAuthRepository.findById(String.valueOf(authentication.getPrincipal()))
                .orElseThrow(() -> new BadCredentialsException("아이디 오류"));

        log.debug("memberAuth: " + memberAuth);

        // user가 입력한 pw와(authentication) DB에서 ID로 조회해온 memberAuth의 pw(credentials)가 같지 않으면
        if(!encoder.matches(authentication.getCredentials().toString(), memberAuth.getPw())){
            log.debug("비밀번호 오류");
            throw new BadCredentialsException("비밀번호 오류");
        }

        log.debug("비밀번호 확인 성공!");

        // PasswordAuthenticationToken token = new PasswordAuthenticationToken(memberAuth.getId(), memberAuth.getPw(), Collections.singleton(new SimpleGrantedAuthority(memberAuth.getRole())));
        // 현 시점으론 role이 필요 없다.
        // 아무튼 조상 생성자(UsernamePasswordAuthenticationToken)를 호출하는 코드
        // 조상의 principal, credentials를 채움
        PasswordAuthenticationToken token = new PasswordAuthenticationToken(memberAuth.getId(), memberAuth.getPw());

        // token 자신의 data 세팅
        token.setId(memberAuth.getId());
        token.setName(memberAuth.getName());

        return token;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(PasswordAuthenticationToken.class);
    }
}
