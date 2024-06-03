package xyz.notagardener.authentication.service;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import xyz.notagardener.authentication.dto.AccessToken;
import xyz.notagardener.authentication.dto.RefreshToken;
import xyz.notagardener.authentication.model.ActiveGardener;
import xyz.notagardener.authentication.repository.ActiveGardenerRepository;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("토큰 컴포넌트 테스트")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TokenProviderImplTest {

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private ActiveGardenerRepository activeGardenerRepository;

    @Autowired
    private TokenProviderImpl tokenProvider;

    @Value("${secret}")
    private String secret;

    private Key key;

    @BeforeAll
    void setKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    @Test
    @DisplayName("액세스 토큰 생성")
    void createAccessToken_ValidInput_Success() {
        // Given
        Long gardenerId = 1L;
        String name = "메밀";

        // When
        AccessToken accessToken = tokenProvider.createAccessToken(gardenerId, name);

        // Then
        assertNotNull(accessToken);
        assertNotNull(accessToken.getKey());
        assertNotNull(accessToken.getToken());
    }

    @Test
    @DisplayName("bearer 토큰 -> Access Token 객체")
    void convertAccessToken_WhenValid_ReturnAccessTokenObject() {
        // Given
        String token = tokenProvider.createAccessToken(1L, "메밀").getToken();

        // When
        AccessToken accessToken = tokenProvider.convertAuthToken(token);

        // Then
        assertNotNull(accessToken);
        assertEquals(token, accessToken.getToken());
    }

    @Test
    @DisplayName("유효한 토큰일 시 Authentication 객체 생성")
    public void getAuthentication_WhenTokenIsValid_ShouldReturnAuthentication() {
        AccessToken token = tokenProvider.createAccessToken(1L, "메밀");
        Authentication authentication = tokenProvider.getAuthentication(token);

        // Then
        assertNotNull(authentication);
        assertTrue(authentication.isAuthenticated());
        assertNull(authentication.getCredentials());
        assertEquals(1, authentication.getAuthorities().size());
        assertTrue(authentication.getAuthorities().contains(new SimpleGrantedAuthority("USER")));
    }

    @Test
    @DisplayName("만료된 토큰: ExpiredJwtException")
    public void getAuthentication_WhenTokenExpired_ShouldThrowExpiredJwtException() {
        LocalDateTime expired = LocalDateTime.now().minusMinutes(3); // 3분 전 만료

        Date expiredDate = Date.from(expired.atZone(ZoneId.systemDefault()).toInstant());
        String expiredToken = Jwts.builder()
                .setSubject(String.valueOf(1))
                .addClaims(new DefaultClaims())
                .signWith(key, SignatureAlgorithm.HS256)
                .setExpiration(expiredDate)
                .compact();

        // When, Then
        assertThrows(ExpiredJwtException.class, () -> tokenProvider.getAuthentication(new AccessToken(expiredToken, key)));
    }

    @Test
    @DisplayName("토큰이 올바르지 않을 때: MalformedJwtException")
    void getAuthentication_WhenTokenInvalid_ShouldThrowExpiredJwtException() {
        String invalidToken = "this_is_invalid_token";
        assertThrows(MalformedJwtException.class, () -> tokenProvider.getAuthentication(new AccessToken(invalidToken, key)));
    }

    @Test
    @DisplayName("새로운 리프레쉬 토큰 만들기: 성공")
    void createRefreshToken_ValidInput_Success() {
        // Given
        Long gardenerId = 1L;
        String name = "메밀";
        String provider = null;

        // When
        RefreshToken refreshToken = tokenProvider.createRefreshToken(gardenerId, name, provider);

        // Then
        assertNotNull(refreshToken);
        verify(activeGardenerRepository, times(1)).save(any(ActiveGardener.class));
    }
}