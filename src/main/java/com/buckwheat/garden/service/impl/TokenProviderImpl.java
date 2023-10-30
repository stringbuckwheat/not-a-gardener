package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.token.AccessToken;
import com.buckwheat.garden.data.token.ActiveGardener;
import com.buckwheat.garden.data.token.RefreshToken;
import com.buckwheat.garden.data.token.UserPrincipal;
import com.buckwheat.garden.repository.RedisRepository;
import com.buckwheat.garden.service.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProviderImpl implements TokenProvider {

    // property의 값을 읽어오는 어노테이션
    @Value("${secret}")
    private String secret;
    private Key key;

    private final UserDetailsService userDetailsService;
    private final RedisRepository redisRepository;

    @PostConstruct // 의존성 주입 후 초기화
    public void init() {
        // base64를 byte[]로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        // byte[]로 Key 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public AccessToken createAccessToken(Long gardenerId, String name){
        // claims 만들기
        Map<String, String> claims = new HashMap<>();

        claims.put("iss", "buckwheat");
        claims.put("aud", name); // 토큰 대상자
        claims.put("exp", LocalDateTime.now().toString());

        return new AccessToken(gardenerId, key, claims);
    }

    /**
     * JwtFilter에서 사용
     * 헤더에서 받아온 token을 주면 이 클래스의 멤버변수로 지정된 key 값을 포함하여 JwtAuthToken 객체 리턴
     * 유효한 토큰인지 확인하기 위해 쓴다.
     *
     * @param token 헤더에서 받아온 token
     * @return JwtAuthToken 객체
     */
    @Override
    public AccessToken convertAuthToken(String token) {
        return new AccessToken(token, key);
    }

    /**
     * JwtFilter에서 유효한 토큰인지를 확인한 후 Security Context에 저장할 Authentication 리턴
     *
     * @param authToken 헤더에 담겨 온 Jwt를 decode한 것
     * @return UsernamePasswordAuthenticationToken(user, null, role)
     */
    @Override
    public Authentication getAuthentication(AccessToken authToken) {
        if (authToken.validate()) {
            // authToken에 담긴 데이터를 받아온다
            Claims claims = authToken.getData();

            UserPrincipal user = (UserPrincipal) userDetailsService.loadUserByUsername(claims.getSubject());

            // 권한 없으면 authenticate false => too many redirect 오류 발생
            // principal, credential, role 다 쓰는 생성자 써야 super.setAuthenticated(true); 호출됨!
            return new UsernamePasswordAuthenticationToken(
                    user,
                    null,
                    Collections.singleton(new SimpleGrantedAuthority("USER")));
        } else {
            throw new JwtException("token error!");
        }
    }

    public RefreshToken createRefreshToken(Long gardenerId, String name){
        RefreshToken refreshToken = new RefreshToken();
        redisRepository.save(new ActiveGardener(gardenerId, name, refreshToken, LocalDateTime.now()));

        return refreshToken;
    }
}
