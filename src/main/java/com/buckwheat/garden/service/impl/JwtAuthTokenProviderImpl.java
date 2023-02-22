package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.config.oauth2.UserPrincipal;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.data.token.JwtAuthToken;
import com.buckwheat.garden.service.JwtAuthTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthTokenProviderImpl implements JwtAuthTokenProvider {

    // property의 값을 읽어오는 어노테이션
    @Value("${secret}")
    private String secret;
    private Key key;

    private final UserDetailsService userDetailsService;

    @PostConstruct // 의존성 주입 후 초기화
    public void init(){
        // base64를 byte[]로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        // byte[]로 Key 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * 일반/소셜 로그인 성공 시 UserPrincipal을 만들어 전달하면 JwtAuthToken 발급
     * LoginService, OAuth2SuccessHandler에서 사용
     * @param userPrincipal Authenticaion에 넣어 Security Context에 저장할 유저 정보
     * @return JwtAuthToken 객체
     */
    @Override
    public JwtAuthToken createAuthToken(UserPrincipal userPrincipal){
        // PK
        int memberNo = userPrincipal.getMember().getMemberNo();

        // claims 만들기
        Map<String, String> claims = new HashMap<>();

        claims.put("memberNo", String.valueOf(memberNo));
        claims.put("email", userPrincipal.getMember().getEmail());
        claims.put("name", userPrincipal.getMember().getName());

        // 기한
        Date expiredDate = Date.from(LocalDateTime.now().plusMinutes(180).atZone(ZoneId.systemDefault()).toInstant());

        return new JwtAuthToken(String.valueOf(memberNo), key, claims, expiredDate);
    }

    /**
     * JwtFilter에서 사용
     * 헤더에서 받아온 token을 주면 이 클래스의 멤버변수로 지정된 key 값을 포함하여 JwtAuthToken 객체 리턴
     * 유효한 토큰인지 확인하기 위해 쓴다.
     * @param token 헤더에서 받아온 token
     * @return JwtAuthToken 객체
     */
    @Override
    public JwtAuthToken convertAuthToken(String token) {
        return new JwtAuthToken(token, key);
    }

    /**
     * JwtFilter에서 유효한 토큰인지를 확인한 후 Security Context에 저장할 Authentication 리턴
     * @param authToken 헤더에 담겨 온 Jwt를 decode한 것
     * @return UsernamePasswordAuthenticationToken(userPrincipal, null, role)
     */
    @Override
    public Authentication getAuthentication(JwtAuthToken authToken) {
        if(authToken.validate()){
            // authToken에 담긴 데이터를 받아온다
            Claims claims = authToken.getData();

            // memberNo가 들어있다.
            log.debug("claims.getSubject(): {}", claims.getSubject());

            UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(claims.getSubject());

            // 권한 없으면 authenticate false => too many redirect 오류 발생
            // principal, credential, role 다 쓰는 생성자 써야 super.setAuthenticated(true); 호출됨!
            return new UsernamePasswordAuthenticationToken(
                    userPrincipal,
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("USER")));
        } else {
            throw new JwtException("token error!");
        }
    }
}
