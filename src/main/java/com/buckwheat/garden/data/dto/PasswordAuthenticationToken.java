package com.buckwheat.garden.data.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

// @Data 사용 불가: lombok needs a default constructor in the base class
@Setter
@Getter
@ToString
@Slf4j
public class PasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {
    private String id;
    private String name;

    public PasswordAuthenticationToken(Object principal, Object credentials){
        super(principal, credentials);
    }

    // 혹시나 role이 필요해지면 쓸 코드
    public PasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities){
        super(principal, credentials, authorities);
    }
}
