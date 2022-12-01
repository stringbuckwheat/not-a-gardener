package com.buckwheat.garden.data.dto;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
public class JwtAuthToken {
    public static final String AUTHORITIES_KEY = "role";
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
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
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
