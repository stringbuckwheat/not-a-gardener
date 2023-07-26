package com.buckwheat.garden.data.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Getter
public class AccessToken {
    public static final String AUTHORITIES_KEY = "USER";
    public static final int EXPIRED_AFTER = 15;
    private final String token;
    private final Key key;
    private LocalDateTime expiredAt;

    public AccessToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    public AccessToken(Long id, Key key, Map<String, String> claims) {
        LocalDateTime expiredAt = LocalDateTime.now().plusMinutes(EXPIRED_AFTER);
        Date expiredDate = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

        this.key = key;
        this.expiredAt = expiredAt;
        this.token = createJwtAuthToken(id, claims, expiredDate).get();
    }

    public String getToken() {
        return this.token;
    }

    public Optional<String> createJwtAuthToken(Long id, Map<String, String> claimMap, Date expiredDate) {
        Claims claims = new DefaultClaims(claimMap);

        return Optional.ofNullable(Jwts.builder()
                .setSubject(String.valueOf(id))
                .addClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredDate)
                .compact()
        );
    }

    public boolean validate() {
        return getData() != null;
    }

    public Claims getData() throws ExpiredJwtException {
        return Jwts
                // Returns a new JwtParserBuilder instance that can be configured to create an immutable/thread-safe
                .parserBuilder()
                // JwtAuthTokenProvider에서 받아온 키 세팅
                .setSigningKey(key)
                // JwtParser 객체 리턴
                .build()
                // 토큰을 jws로 파싱
                .parseClaimsJws(token)
                // 앞서 토큰에 저장한 data들이 담긴 claims를 얻어온다.
                // String or a Claims instance
                .getBody();
    }
}
