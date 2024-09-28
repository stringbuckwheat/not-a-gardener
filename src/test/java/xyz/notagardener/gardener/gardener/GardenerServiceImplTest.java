package xyz.notagardener.gardener.gardener;

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
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import xyz.notagardener.authentication.token.AccessToken;
import xyz.notagardener.authentication.dto.Info;
import xyz.notagardener.authentication.dto.Login;
import xyz.notagardener.authentication.token.RefreshToken;
import xyz.notagardener.authentication.service.AuthenticationService;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.HasSameUsernameException;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.gardener.dto.GardenerDetail;
import xyz.notagardener.gardener.dto.Register;
import xyz.notagardener.gardener.dto.VerifyResponse;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.gardener.service.GardenerServiceImpl;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@DisplayName("회원 컴포넌트 테스트")
class GardenerServiceImplTest {
    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private GardenerRepository gardenerRepository;

    @InjectMocks
    private GardenerServiceImpl gardenerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("한 회원의 정보: 성공")
    void getOne_WhenGardenerIdValid_ReturnGardenerDetail() {
        // Given
        Long gardenerId = 1L;
        GardenerDetail expected = GardenerDetail.builder()
                .id(gardenerId)
                .build();

        when(gardenerRepository.findGardenerDetailByGardenerId(gardenerId)).thenReturn(Optional.of(expected));

        // When
        GardenerDetail result = gardenerService.getOne(gardenerId);

        // Then
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("한 회원의 정보 - gardenerId 오류")
    void getOne_WhenGardenerIdNotExist_ThrowUsernameNotFoundException() {
        // Given
        Long gardenerId = 1L;

        when(gardenerRepository.findGardenerDetailByGardenerId(gardenerId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> gardenerService.getOne(gardenerId);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_ACCOUNT, e.getCode());
    }

    @Test
    @DisplayName("신원 확인, 비밀번호 확인: 성공")
    void identify_WhenLoginValid_ReturnTrue() {
        // Given
        Long gardenerId = 1L;
        String username = "testgardener";
        String password = "password1234";

        Gardener gardener = Gardener.builder()
                .gardenerId(gardenerId)
                .username(username)
                .password(password)
                .build();
        Login login = new Login(username, password);

        when(gardenerRepository.findById(gardenerId)).thenReturn(Optional.of(gardener));
        when(encoder.matches(password, gardener.getPassword())).thenReturn(true);

        // When
        VerifyResponse result = gardenerService.identify(gardenerId, login);

        // Then
        assertNotNull(result);
        assertTrue(result.getVerified());
        verify(gardenerRepository, times(1)).findById(gardenerId);
        verify(encoder, times(1)).matches(password, gardener.getPassword());
    }

    @Test
    @DisplayName("신원 확인: 해당 username의 회원 없음")
    void identify_WhenUsernameNotExist_ThrowUsernameNotFoundException() {
        // Given
        Long gardenerId = 1L;
        String username = "testgardener";
        String password = "password1234";

        Login login = new Login(username, password);

        when(gardenerRepository.findById(gardenerId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> gardenerService.identify(gardenerId, login);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_ACCOUNT, e.getCode());
    }

    @Test
    @DisplayName("신원 확인: 비밀번호 불일치")
    void identify_WhenPasswordInvalid_ReturnFalse() {
        // Given
        Long gardenerId = 1L;
        String username = "testgardener";
        String password = "wrongpassword";

        Gardener gardener = Gardener.builder()
                .gardenerId(gardenerId)
                .username(username)
                .password("validpassword")
                .build();

        Login login = new Login(username, password);

        when(gardenerRepository.findById(gardenerId)).thenReturn(Optional.of(gardener));
        when(encoder.matches(password, gardener.getPassword())).thenReturn(false);

        // When
        VerifyResponse result = gardenerService.identify(gardenerId, login);

        // Then
        assertNotNull(result);
        assertFalse(result.getVerified());
        verify(gardenerRepository, times(1)).findById(gardenerId);
        verify(encoder, times(1)).matches(password, gardener.getPassword());
    }

    @Test
    @DisplayName("아이디 중복 검사: 중복 아이디")
    void hasSameUsername_WhenInvalid_ReturnThatUsername() {
        // Given
        String username = "testgardener";

        when(gardenerRepository.findByProviderIsNullAndUsername(username))
                .thenReturn(Optional.of(Gardener.builder().username(username).build()));

        // When, Then
        HasSameUsernameException e = assertThrows(HasSameUsernameException.class, () -> gardenerService.hasSameUsername(username));
        assertEquals(ExceptionCode.HAS_SAME_USERNAME, e.getCode());
    }

    @Test
    @DisplayName("아이디 중복 검사: 사용 가능한 아이디")
    void hasSameUsername_WhenValid_ReturnNull() {
        // Given
        String username = "testgardener";
        when(gardenerRepository.findByProviderIsNullAndUsername(username)).thenReturn(Optional.empty());

        // When
        String result = gardenerService.hasSameUsername(username);

        // Then
        assertNotNull(result);
        assertEquals(username, result);
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


        String secret = RandomStringUtils.random(50, 97, 122, true, true);
        Key testKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));

        AccessToken accessToken = new AccessToken(savedGardener.getGardenerId(), testKey, new HashMap<String, String>());
        RefreshToken refreshToken = new RefreshToken();

        Info expectedInfo = new Info(accessToken.getToken(), refreshToken.getToken(), savedGardener);

        when(gardenerRepository.findByProviderIsNullAndUsername(username)).thenReturn(Optional.empty());
        when(gardenerRepository.save(any())).thenReturn(savedGardener);
        when(authenticationService.setAuthentication(savedGardener)).thenReturn(expectedInfo);

        // When
        Info result = gardenerService.add(register);

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
                .thenThrow(HasSameUsernameException.class);

        // When
        Executable executable = () -> gardenerService.add(register);

        // Then
        HasSameUsernameException e = assertThrows(HasSameUsernameException.class, executable);
    }

    @Test
    @DisplayName("비밀번호 변경: 성공")
    void updatePassword_WhenValid() {
        // Given
        Long id = 1L;
        String username = "testgardener";
        String password = "newPassword";
        String prevPassword = "prevpassword";
        String encryptedPassword = "encryptedPassword";

        Login login = new Login(username, password);
        Gardener gardener = Gardener.builder().username(username).password(prevPassword).build();

        when(gardenerRepository.findById(id)).thenReturn(Optional.of(gardener));
        when(encoder.encode(password)).thenReturn(encryptedPassword);

        // When
        gardenerService.updatePassword(id, login);

        // Then
        assertNotEquals(prevPassword, gardener.getPassword());
    }

    @Test
    @DisplayName("비밀번호 변경: 실패(PK 오류)")
    void updatePassword_WhenGardenerIdNotExist_ThrowUsernameNotFoundException() {
        // Given
        Long id = 1L;
        String username = "testgardener";
        String password = "newPassword";

        Login login = new Login(username, password);

        when(gardenerRepository.findById(id)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> gardenerService.updatePassword(id, login);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_ACCOUNT, e.getCode());
    }

    @Test
    @DisplayName("회원 정보 변경: 성공")
    void update_WhenValid_ReturnGardenerDetail() {
        // Given
        Long gardenerId = 1L;

        String prevEmail = "prevmail@notagardener.xyz";
        String prevName = "메밀";

        String newEmail = "newmail@notagardener.xyz";
        String newName = "호밀";

        Gardener gardener = Gardener.builder()
                .gardenerId(gardenerId)
                .username("testgardener")
                .email(prevEmail)
                .name(prevName)
                .createDate(LocalDateTime.now())
                .provider(null)
                .build();

        GardenerDetail request = GardenerDetail.builder()
                .id(1L)
                .username("testgardener")
                .email(newEmail)
                .name(newName)
                .build();

        when(gardenerRepository.findById(gardenerId)).thenReturn(Optional.of(gardener));

        // When
        GardenerDetail result = gardenerService.update(request);

        // Then
        assertEquals(newEmail, result.getEmail());
        assertEquals(newName, result.getName());
    }

    @Test
    @DisplayName("회원 정보 변경: gardenerId 오류")
    void update_WhenGardenerIdNotExist_ThrowsUsernameNotFoundException() {
        // Given
        GardenerDetail request = GardenerDetail.builder()
                .id(1L)
                .username("testgardener")
                .email("newmail@notagardener.xyz")
                .name("호밀")
                .build();

        when(gardenerRepository.findById(1L)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () ->  gardenerService.update(request);
        UsernameNotFoundException e = assertThrows(UsernameNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_ACCOUNT.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("회원 탈퇴")
    void delete() {
        // Given
        Long gardenerId = 1L;
        doNothing().when(gardenerRepository).deleteById(gardenerId);

        // When
        gardenerService.delete(gardenerId);

        // Then
        verify(gardenerRepository, times(1)).deleteById(gardenerId);
    }
}