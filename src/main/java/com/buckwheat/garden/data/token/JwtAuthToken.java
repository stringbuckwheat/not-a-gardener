package com.buckwheat.garden.data.token;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class JwtAuthToken {
    public static final String AUTHORITIES_KEY = "user";
    private final String token;
    private final Key key;

    public JwtAuthToken(String token, Key key) {
        this.token = token;
        this.key = key;
    }

    public JwtAuthToken(String id, Key key, Map<String, String> claims, Date expiredDate){
        this.key = key;
        this.token = createJwtAuthToken(id, claims, expiredDate).get();
    }

    public String getToken(){
        // return token.token;
        return this.token;
    }

    public Optional<String> createJwtAuthToken(String id, Map<String, String> claimMap, Date expiredDate){
        Claims claims = new DefaultClaims(claimMap);
        // claims.put(JwtAuthToken.AUTHORITIES_KEY, role);

        // ofNullable(): 말그대로 null을 허용
        return Optional.ofNullable(Jwts.builder()
                .setSubject(id)
                .addClaims(claims)
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredDate)
                .compact()
        );
    }

    public boolean validate(){
        return getData() != null;
    }

    public Claims getData(){
        try{
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
        } catch(SecurityException e){
            log.info("Invalid JWT signature.");
        } catch(MalformedJwtException e){
            log.info("Invalid JWT token.");
        } catch(ExpiredJwtException e){
            log.info("Expired JWT token.");
        } catch(UnsupportedJwtException e){
            log.info("Unsupported JWT token.");
        } catch(IllegalArgumentException e){
            log.info("JWT token compact of handler are invalid");
        }

        return null;
    }
}
