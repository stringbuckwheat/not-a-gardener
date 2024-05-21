package xyz.notagardener.authentication.dto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Getter
@ToString
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

        return Optional.ofNullable(
                Jwts.builder()
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

    public Claims getData() {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}
