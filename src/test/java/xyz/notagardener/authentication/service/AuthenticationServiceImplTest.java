package xyz.notagardener.authentication.service;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.gardener.GardenerRepository;
import xyz.notagardener.gardener.gardener.Register;

import java.security.Key;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
    @DisplayName("아이디 중복 검사: 중복 아이디")
    void hasSameUsername_WhenInvalid_ReturnThatUsername() {
        // Given
        String username = "testgardener";

        when(gardenerRepository.findByProviderIsNullAndUsername(username))
                .thenReturn(Optional.of(Gardener.builder().username(username).build()));

        // When
        String result = authenticationService.hasSameUsername(username);

        // Then
        assertNotNull(result);
    }

    @Test
    @DisplayName("아이디 중복 검사: 사용 가능한 아이디")
    void hasSameUsername_WhenValid_ReturnNull() {
        // Given
        String username = "testgardener";
        when(gardenerRepository.findByProviderIsNullAndUsername(username)).thenReturn(Optional.empty());

        // When
        String result = authenticationService.hasSameUsername(username);

        // Then
        assertNull(result);
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
    @DisplayName("회원가입: Happy Path")
    void register_WhenValid_ReturnToken() {
        // Given
        String username = "testgardener";
        String email = "testgardener@gmail.com";
        String password = "testpassword1234!";
        String name = "메밀";

        Register register = new Register(username, email, password, name);
        Gardener savedGardener = Gardener.builder()
                .gardenerId(1L)
                .name(name)
                .provider(null)
                .build();

        AccessToken accessToken = mockAccessToken(savedGardener.getGardenerId());
        RefreshToken refreshToken = new RefreshToken();

        Info expectedInfo = new Info(accessToken.getToken(), refreshToken.getToken(), savedGardener);

        when(gardenerRepository.findByProviderIsNullAndUsername(username)).thenReturn(Optional.empty());
        when(gardenerRepository.save(any())).thenReturn(savedGardener);
        when(tokenProvider.createAccessToken(savedGardener.getGardenerId(), savedGardener.getName())).thenReturn(accessToken);
        when(tokenProvider.createRefreshToken(savedGardener.getGardenerId(), savedGardener.getName(), savedGardener.getProvider()))
                .thenReturn(refreshToken);

        // When
        Info result = authenticationService.add(register);

        assertEquals(expectedInfo, result);
        assertEquals(accessToken.getToken(), result.getToken().getAccessToken()); // 액세스 토큰
        assertEquals(refreshToken.getToken(), result.getToken().getRefreshToken()); // 리프레쉬 토큰
    }

    @Test
    @DisplayName("회원 가입: username 중복")
    void register_WhenSameUsernameExist_ThrowIllegalArgumentException() {
        String username = "testgardener";
        Register register = new Register(username, "email", "password", "name");

        when(gardenerRepository.findByProviderIsNullAndUsername(username))
                .thenReturn(Optional.of(Gardener.builder().username(username).build()));

        // When
        Executable executable = () -> authenticationService.add(register);

        // Then
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(ExceptionCode.INVALID_REQUEST_DATA.getCode(), e.getMessage());
    }

    static Stream<Register> invalidRegisterData() {
        String validUsername = "testgardener";
        String validEmail = "testgardener@gmail.com";
        String validPassword = "testpassword1234!";
        String validName = "메밀";

        return Stream.of(
                new Register(null, validEmail, validPassword, validName),
                new Register("", validEmail, validPassword, validName),
                new Register("a", validEmail, validPassword, validName),
                new Register("aweoifg3289rh8rejkhwejgfhwejf", validEmail, validPassword, validName),
                new Register("가나다라마", validEmail, validPassword, validName),

                new Register(validUsername, null, validPassword, validName),
                new Register(validUsername, "", validPassword, validName),
                new Register(validUsername, "null", validPassword, validName),
                new Register(validUsername, "@gmail.com", validPassword, validName),
                new Register(validUsername, "hi@gmail", validPassword, validName),
                new Register(validUsername, "hi#gmail", validPassword, validName),

                new Register(validUsername, validEmail, null, validName),
                new Register(validUsername, validEmail, "", validName),
                new Register(validUsername, validEmail, "1", validName),
                new Register(validUsername, validEmail, "password", validName),
                new Register(validUsername, validEmail, "reallyreallyreallylongpasswordisinvalid", validName),

                new Register(validUsername, validEmail, validPassword, null),
                new Register(validUsername, validEmail, validPassword, ""),
                new Register(validUsername, validEmail, validPassword, "reallyreallyreallylongnameisinvalid")
        );
    }

    @ParameterizedTest
    @MethodSource("invalidRegisterData")
    void testInvalidRegisterData(Register invalidRegister) {
        // Given
        Executable executable = () -> authenticationService.add(invalidRegister);

        // When, Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(ExceptionCode.INVALID_REQUEST_DATA.getCode(), exception.getMessage());
    }

    @Test
    @DisplayName("Silent Refresh: Happy Path")
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