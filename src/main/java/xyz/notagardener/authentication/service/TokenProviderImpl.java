package xyz.notagardener.authentication.service;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.authentication.dto.AccessToken;
import xyz.notagardener.authentication.dto.RefreshToken;
import xyz.notagardener.authentication.model.ActiveGardener;
import xyz.notagardener.authentication.model.UserPrincipal;
import xyz.notagardener.authentication.repository.ActiveGardenerRepository;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenProviderImpl implements TokenProvider {

    // property의 값을 읽어오는 어노테이션
    @Value("${secret}")
    private String secret;
    private Key key;

    private final UserDetailsService userDetailsService;
    private final ActiveGardenerRepository activeGardenerRepository;

    @PostConstruct
    public void init() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Override
    public AccessToken createAccessToken(Long gardenerId, String name) {
        Map<String, String> claims = Map.of(
                "iss", "buckwheat",
                "aud", name,
                "exp", LocalDateTime.now().toString()
        );

        return new AccessToken(gardenerId, key, claims);
    }

    @Override
    public AccessToken convertAuthToken(String token) {
        return new AccessToken(token, key);
    }

    @Override
    @Transactional(readOnly = true)
    public Authentication getAuthentication(AccessToken authToken) {
        if (!authToken.validate()) {
            throw new JwtException("token error!");
        }

        Claims claims = authToken.getData();
        UserPrincipal user = (UserPrincipal) userDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(
                user,
                null,
                Collections.singleton(new SimpleGrantedAuthority("USER")));
    }

    public RefreshToken createRefreshToken(Long gardenerId, String name, String provider) {
        RefreshToken refreshToken = new RefreshToken();
        activeGardenerRepository.save(new ActiveGardener(gardenerId, name, provider, refreshToken, LocalDateTime.now()));

        return refreshToken;
    }
}
