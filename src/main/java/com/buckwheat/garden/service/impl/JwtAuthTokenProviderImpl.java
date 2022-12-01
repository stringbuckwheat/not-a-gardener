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

    @PostConstruct
    public void init(){
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public JwtAuthToken createAuthToken(String id, Map<String, String> claims, Date expiredDate) {
        return new JwtAuthToken(id, key, claims, expiredDate);
    }

    @Override
    public JwtAuthToken convertAuthToken(String token) {
        return new JwtAuthToken(token, key);
    }

    @Override
    public Authentication getAuthentication(JwtAuthToken authToken) {
        if(authToken.validate()){
            Claims claims = authToken.getData();
            Collection<? extends GrantedAuthority> authorities = Collections.singleton(new SimpleGrantedAuthority(claims.get(JwtAuthToken.AUTHORITIES_KEY, String.class)));
            User principal = new User(claims.getSubject(), "", authorities);

            return new UsernamePasswordAuthenticationToken(principal, authToken, authorities);
        } else {
            throw new JwtException("token error!");
        }
    }
}
