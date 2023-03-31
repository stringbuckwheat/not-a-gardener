package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.repository.MemberRepository;
import com.buckwheat.garden.service.JwtAuthTokenProvider;
import com.buckwheat.garden.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final JwtAuthTokenProvider tokenProvider;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    /**
     * parameter로 받은 id가 db에 존재하면 그 id를 돌려주고, 없으면 null을 리턴
     * @param id
     * @return
     */
    @Override
    public String getIdByInputId(String id) {
        Optional<Member> member = memberRepository.findByUsername(id);

        if (member.isEmpty()) {
            return null;
        }

        return member.get().getUsername();
    }

    @Override
    public MemberDto.MemberInfo addMember(MemberDto.RegisterDto paramRegisterDto) {
        // DTO에 암호화된 비밀번호 저장하기
        paramRegisterDto.encryptPassword(encoder.encode(paramRegisterDto.getPw()));

        Member member = memberRepository.save(paramRegisterDto.toEntity());
        return setAuthentication(member);
    }



    /**
     * MemberDto의 ID, PW를 사용하여 DB를 통한 인증
     * 인증 성공 시 그 과정에서 받아온 Member 객체를 사용해 UserPrincipal을 만들고,
     * UsernamePasswordAuthenticationToken를 Security Context에 저장
     * 이후 JwtAuthToken을 생성 후 리턴한다.
     *
     * @param login
     * @return JwtAuthToken을 인코딩한 String 값 리턴
     */
    @Override
    public MemberDto.MemberInfo login(MemberDto.Login login) {
        Member member = memberRepository.findByUsername(login.getUsername())
                .orElseThrow(() -> new BadCredentialsException("아이디 오류"));

        // 비밀번호 일치 여부 검사
        if (!encoder.matches(login.getPw(), member.getPw())) {
            throw new BadCredentialsException("비밀번호 오류");
        }

        return setAuthentication(member);
    }

    // 인증 성공 후 Security Context에 유저 정보를 저장하고 토큰과 기본 정보를 리턴
    MemberDto.MemberInfo setAuthentication(Member member) {
        // member 객체를 포함한 userPrincipal 생성
        UserPrincipal userPrincipal = UserPrincipal.create(member);

        // Authentication에 담을 토큰 생성
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(userPrincipal, null, Collections.singleton(new SimpleGrantedAuthority("USER")));

        // security context에 저장
        SecurityContextHolder.getContext().setAuthentication(token);

        // 인코딩된 값 리턴
        String jwtToken = tokenProvider.createAuthToken(userPrincipal).getToken();

        // return new MemberInfo(jwtToken, member.getMemberNo(), member.getName());
        return MemberDto.MemberInfo.from(jwtToken, member);
    }
}
