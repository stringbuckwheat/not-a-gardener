package xyz.notagardener.authentication.service;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import xyz.notagardener.authentication.dto.*;
import xyz.notagardener.authentication.model.ActiveGardener;
import xyz.notagardener.authentication.repository.ActiveGardenerRepository;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ExpiredRefreshTokenException;
import xyz.notagardener.common.error.exception.GardenerNotInSessionException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.repository.GardenerRepository;

import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@DisplayName("인증 컴포넌트 테스트")
class AuthenticationServiceImplTest {
    @Mock
    private TokenProvider tokenProvider;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private ActiveGardenerRepository activeGardenerRepository;

    @Mock
    private GardenerRepository gardenerRepository;

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    @DisplayName("가드너 정보 가져오기")
    void getGardenerInfo_WhenValidGardenerId_ReturnGardenerInfo() {
        // Given
        Long gardenerId = 1L;
        String name = "메밀";
        RefreshToken refreshToken = new RefreshToken();

        ActiveGardener activeGardener = new ActiveGardener(gardenerId, name, null, refreshToken, LocalDateTime.now());
        Gardener gardener = Gardener.builder().gardenerId(gardenerId).name(name).build();

        when(gardenerRepository.findById(gardenerId)).thenReturn(Optional.of(gardener));
        when(activeGardenerRepository.findById(gardenerId)).thenReturn(Optional.of(activeGardener));

        // When
        Info result = authenticationService.getGardenerInfo(gardenerId);

        // Then
        assertNotNull(result);
        assertEquals(gardener.getGardenerId(), result.getInfo().getGardenerId());
        assertEquals(gardener.getName(), result.getInfo().getName());
        assertEquals(refreshToken.getToken(), result.getToken().getRefreshToken());
    }

    @Test
    @DisplayName("가드너 정보 가져오기: Redis에 사용자 정보 없음 - 실패")
    void getGardenerInfo_WhenGardenerNotExistInRedis_ThrowGardenerNotInSessionException() {
        // Given
        Long gardenerId = 1L;
        String name = "메밀";

        Gardener gardener = Gardener.builder().gardenerId(gardenerId).name(name).build();

        when(gardenerRepository.findById(gardenerId)).thenReturn(Optional.of(gardener));
        when(activeGardenerRepository.findById(gardenerId)).thenReturn(Optional.empty());

        // When
        Executable executable = () -> authenticationService.getGardenerInfo(gardenerId);
        GardenerNotInSessionException e = assertThrows(GardenerNotInSessionException.class, executable);
        assertEquals(ExceptionCode.NO_TOKEN_IN_REDIS, e.getCode());
    }


    private AccessToken mockAccessToken(Long gardenerId) {
        //// 가짜 키 만들기
        String secret = RandomStringUtils.random(50, 97, 122, true, true);
        Key testKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        return new AccessToken(gardenerId, testKey, new HashMap<String, String>());
    }

    @Test
    @DisplayName("로그인: Happy Path")
    void login_WhenSuccess_ReturnToken() {
        // Given
        String username = "testgardener";
        String password = "testpassword";

        //// 유저 정보
        Login login = new Login(username, password);
        Gardener gardener = Gardener.builder()
                .gardenerId(1L)
                .name("메밀")
                .provider(null)
                .build();

        //// 토큰 객체들
        AccessToken accessToken = mockAccessToken(gardener.getGardenerId());
        RefreshToken refreshToken = new RefreshToken();

        when(gardenerRepository.findByProviderIsNullAndUsername(username)).thenReturn(Optional.of(gardener));
        when(encoder.matches(password, gardener.getPassword())).thenReturn(true);
        when(tokenProvider.createAccessToken(any(), any())).thenReturn(accessToken);
        when(tokenProvider.createRefreshToken(any(), any(), any())).thenReturn(refreshToken);

        Info expectedInfo = new Info(accessToken.getToken(), refreshToken.getToken(), gardener);

        // When
        Info result = authenticationService.login(login);

        // Then
        assertNotNull(result);
        assertEquals(LocalDate.now(), gardener.getRecentLogin().toLocalDate()); // 최근 로그인 - 날짜만 검사
        assertEquals(expectedInfo, result);
    }

    @Test
    @DisplayName("로그인: 해당 아이디 없음")
    void login_WhenUsernameNotFound_ThrowUsernameNotFoundException() {
        // Given
        String username = "wrongusername";
        String password = "testpassword";
        Login login = new Login(username, password);

        when(gardenerRepository.findByProviderIsNullAndUsername(username)).thenReturn(Optional.empty());

        // When
        Executable executable = () -> authenticationService.login(login);

        // Then
        assertThrows(UsernameNotFoundException.class, executable);
    }

    @Test
    @DisplayName("로그인: 패스워드 틀림")
    void login_WhenPasswordInvalid_ThrowBadCredentialException() {
        // Given
        String username = "testgardener";
        String password = "wrongpassword";
        Login login = new Login(username, password);

        Gardener gardener = Gardener.builder().password("rightpassword").build();

        when(gardenerRepository.findByProviderIsNullAndUsername(username)).thenReturn(Optional.of(gardener));
        when(encoder.matches(password, gardener.getPassword())).thenReturn(false);

        // When
        Executable executable = () -> authenticationService.login(login);

        // Then
        assertThrows(BadCredentialsException.class, executable);
    }

    @Test
    @DisplayName("Silent Refresh: 성공")
    void refreshAccessToken_WhenValid_ReturnNewAccessTokenAndRefreshToken() {
        // Given
        RefreshToken refreshToken = new RefreshToken();
        long gardenerId = 1L;
        String name = "메밀";

        ActiveGardener activeGardener = new ActiveGardener(gardenerId, name, null, refreshToken, LocalDateTime.now());

        when(activeGardenerRepository.findById(gardenerId)).thenReturn(Optional.of(activeGardener));
        when(tokenProvider.createAccessToken(gardenerId, name)).thenReturn(new AccessToken("new_access_token", null));

        // When
        Token newToken = authenticationService.refreshAccessToken(new Refresh(gardenerId, refreshToken.getToken()));

        // Then
        assertNotNull(newToken);
        assertEquals("new_access_token", newToken.getAccessToken());
        assertNotEquals(refreshToken, newToken.getRefreshToken());
    }

    @Test
    @DisplayName("Silent Refresh: 현재 로그인하지 않은 사용자 (유효하지 않은 gardenerId)")
    void refreshAccessToken_WhenGardenerIdNotExistInRedis_ThrowsGardenerNotInSessionException() {
        // Given
        Long invalidGardenerId = 1L;
        String refreshToken = "refresh_token";

        when(activeGardenerRepository.findById(invalidGardenerId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> authenticationService.refreshAccessToken(new Refresh(invalidGardenerId, refreshToken));
        GardenerNotInSessionException e = assertThrows(GardenerNotInSessionException.class, executable);
        assertEquals(ExceptionCode.NO_TOKEN_IN_REDIS, e.getCode());
    }

    @Test
    @DisplayName("Silent Refresh: 유효하지 않은 Refresh Token")
    void refreshAccessToken_WhenRefreshTokenInvalid_ThrowsBadCredentialsException() {
        // Given
        Long gardenerId = 1L;
        String invalidRefreshToken = "invalid_refresh_token";
        ActiveGardener activeGardener = new ActiveGardener(gardenerId, "메밀", null, new RefreshToken(), LocalDateTime.now());

        when(activeGardenerRepository.findById(gardenerId)).thenReturn(Optional.of(activeGardener));

        // When, Then
        Executable executable = () -> authenticationService.refreshAccessToken(new Refresh(gardenerId, invalidRefreshToken));
        BadCredentialsException e = assertThrows(BadCredentialsException.class, executable);
        assertEquals(ExceptionCode.INVALID_REFRESH_TOKEN.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("Silent Refresh: Refresh Token 기한 만료")
    void refreshAccessToken_WhenRefreshTokenExpired_ThrowsBadCredentialsException() {
        // Given
        Long gardenerId = 1L;
        RefreshToken expiredToken = new RefreshToken("expired_token", LocalDateTime.now().minusSeconds(1)); // 1초 전 만료
        ActiveGardener activeGardener = new ActiveGardener(gardenerId, "메밀", null, expiredToken, LocalDateTime.now());

        when(activeGardenerRepository.findById(gardenerId)).thenReturn(Optional.of(activeGardener));

        // When, Then
        Executable executable = () -> authenticationService.refreshAccessToken(new Refresh(gardenerId, "expired_token"));
        ExpiredRefreshTokenException e = assertThrows(ExpiredRefreshTokenException.class, executable);
        assertEquals(ExceptionCode.REFRESH_TOKEN_EXPIRED, e.getCode());
    }

    @Test
    @DisplayName("로그아웃")
    void logout() {
        // Given
        Long id = 1L;

        // When
        authenticationService.logOut(id);

        // Then
        verify(activeGardenerRepository).deleteById(id);
    }
}