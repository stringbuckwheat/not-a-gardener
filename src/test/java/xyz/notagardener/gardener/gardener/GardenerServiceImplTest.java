package xyz.notagardener.gardener.gardener;

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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.authentication.dto.Login;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;

@DisplayName("가드너")
class GardenerServiceImplTest {
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
    @DisplayName("한 회원의 정보")
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
    void getOne_WhenGardenerIdNotExist_ThrowNoSuchElementException() {
        // Given
        Long gardenerId = 1L;

        when(gardenerRepository.findGardenerDetailByGardenerId(gardenerId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> gardenerService.getOne(gardenerId);
        assertThrows(NoSuchElementException.class, executable);
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
        boolean result = gardenerService.identify(gardenerId, login);

        // Then
        assertTrue(result);
        verify(gardenerRepository, times(1)).findById(gardenerId);
        verify(encoder, times(1)).matches(password, gardener.getPassword());
    }

    @Test
    @DisplayName("신원 확인: 해당 username의 회원 없음")
    void identify_WhenUsernameNotExist_ThrowBadCredentialException() {
        // Given
        Long gardenerId = 1L;
        String username = "testgardener";
        String password = "password1234";

        Login login = new Login(username, password);

        when(gardenerRepository.findById(gardenerId)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> gardenerService.identify(gardenerId, login);
        BadCredentialsException e = assertThrows(BadCredentialsException.class, executable);
        assertEquals(ExceptionCode.WRONG_PASSWORD.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("신원 확인 -> 비밀번호 불일치")
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
        boolean result = gardenerService.identify(gardenerId, login);

        // Then
        assertFalse(result);
        verify(gardenerRepository, times(1)).findById(gardenerId);
        verify(encoder, times(1)).matches(password, gardener.getPassword());
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
    void updatePassword_WhenGardenerIdNotExist_ThrowBadCredentialException() {
        // Given
        Long id = 1L;
        String username = "testgardener";
        String password = "newPassword";

        Login login = new Login(username, password);

        when(gardenerRepository.findById(id)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> gardenerService.updatePassword(id, login);
        BadCredentialsException e = assertThrows(BadCredentialsException.class, executable);
        assertEquals(ExceptionCode.WRONG_ACCOUNT.getCode(), e.getMessage());
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

    static Stream<GardenerDetail> invalidUpdateData() {
        String validEmail = "validaddress@notagardener.xyz";
        String validName = "메밀";

        return Stream.of(
                GardenerDetail.builder().id(1L).username("gardener").email(null).name(validName).build(),
                GardenerDetail.builder().id(1L).username("gardener").email("").name(validName).build(),
                GardenerDetail.builder().id(1L).username("gardener").email("null").name(validName).build(),
                GardenerDetail.builder().id(1L).username("gardener").email("@gmail.com").name(validName).build(),
                GardenerDetail.builder().id(1L).username("gardener").email("hi@gmail").name(validName).build(),
                GardenerDetail.builder().id(1L).username("gardener").email("hi#gmail").name(validName).build(),

                GardenerDetail.builder().id(1L).username("gardener").email(validEmail).name(null).build(),
                GardenerDetail.builder().id(1L).username("gardener").email(validEmail).name("").build(),
                GardenerDetail.builder().id(1L).username("gardener").email(validEmail).name("reallyreallyreallylongnameisinvalid").build()
        );
    }

    @Test
    @DisplayName("회원 정보 변경: gardenerId 오류")
    void update_WhenGardenerIdNotExist_ThrowsNoSuchElementException() {
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
        BadCredentialsException e = assertThrows(BadCredentialsException.class, executable);
        assertEquals(ExceptionCode.WRONG_ACCOUNT.getCode(), e.getMessage());
    }

    @ParameterizedTest
    @MethodSource("invalidUpdateData")
    @DisplayName("회원 정보 변경: 유효하지 않은 이름, 이메일")
    void update_WhenNameAndEmailInValid_ThrowsIllegalArgumentException(GardenerDetail invalidRequest) {
        Executable executable = () -> gardenerService.update(invalidRequest);
        IllegalArgumentException e = assertThrows(IllegalArgumentException.class, executable);
        assertEquals(ExceptionCode.INVALID_REQUEST_DATA.getCode(), e.getMessage());
    }

    @Test
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