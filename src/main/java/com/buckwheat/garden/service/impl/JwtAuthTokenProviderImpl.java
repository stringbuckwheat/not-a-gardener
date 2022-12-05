package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.JwtAuthToken;
import com.buckwheat.garden.service.JwtAuthTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.Key;
import java.util.*;

@Component
public class JwtAuthTokenProviderImpl implements JwtAuthTokenProvider {

    // property의 값을 읽어오는 어노테이션
    @Value("${secret}")
    private String secret;
    private Key key;


    @PostConstruct // 의존성 주입 후 초기화
    public void init(){
        // base64를 byte[]로 변환
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        // byte[]로 Key 생성
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public JwtAuthToken createAuthToken(String id, Map<String, String> claims, Date expiredDate) {
        return new JwtAuthToken(id, key, claims, expiredDate);
    }

    @Override
    public JwtAuthToken convertAuthToken(String token) {
        // 멤버변수로 지정된 key 값을 포함하여 새로운 JwtAuthToken 객체 지정
        return new JwtAuthToken(token, key);
    }

    @Override
    public Authentication getAuthentication(JwtAuthToken authToken) {
        if(authToken.validate()){
            // authToken에 담긴 데이터를 받아온다
            Claims claims = authToken.getData();

            // Colletions.singleton(T o): Returns an immutable set containing only the specified object.
            // SimpleGrantedAuthority: 권한 객체. "user" 같은 String 값을 생성자 파라미터로 넣어주면 된다.
            // claims.get(JwtAuthToken.AUTHORITIES_KEY, String.class): 	get(String claimName, Class<T> requiredType)
            Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(claims.get(JwtAuthToken.AUTHORITIES_KEY, String.class)));

            // public User(String username, String password, Collection<? extends GrantedAuthority> authorities)
            User principal = new User(claims.getSubject(), "", authorities);

            // UsernamePasswordAuthenticationToken(Object principal, Object credentials,
            //			Collection<? extends GrantedAuthority> authorities)
            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        } else {
            throw new JwtException("token error!");
        }
    }
}
