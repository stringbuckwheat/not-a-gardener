package xyz.notagardener.gardener.forgot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.VerificationException;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.authentication.dto.Login;
import xyz.notagardener.gardener.dto.*;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.gardener.repository.LostGardenerRepository;
import xyz.notagardener.gardener.service.ForgotServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("계정 찾기")
class ForgotServiceImplTest {
    @Mock
    private GardenerRepository gardenerRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private BCryptPasswordEncoder encoder;

    @Mock
    private LostGardenerRepository lostGardenerRepository;

    @InjectMocks
    private ForgotServiceImpl forgotService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Username> getMockedUsernames() {
        return Arrays.asList(
                new Username() {
                    @Override
                    public String getUsername() {
                        return "testgardener1";
                    }
                },
                new Username() {
                    @Override
                    public String getUsername() {
                        return "testgardener2";
                    }
                }
        );
    }

    @Test
    @DisplayName("계정 찾기: Happy Path")
    void forgotAccount_WhenEmailValid_SendEmailAndReturnUsernames() {
        // Given
        String email = "stringbuckwheat@gmail.com";
        List<Username> usernames = getMockedUsernames();

        when(gardenerRepository.findByProviderIsNullAndEmail(email)).thenReturn(usernames);

        // When
        Forgot forgot = forgotService.forgotAccount(email);

        // Then
        assertEquals(email, forgot.getEmail());
        assertEquals(usernames, forgot.getUsernames());
    }

    @Test
    @DisplayName("계정 찾기: 해당 이메일로 가입한 계정 없음")
    void forgotAccount_WhenEmailNotExist_ThrowsUsernameNotFoundException() {
        // Given
        String email = "notmyuser@gmail.com";

        when(gardenerRepository.findByProviderIsNullAndEmail(email)).thenReturn(new ArrayList<>());

        // When, Then
        Executable executable = () -> forgotService.forgotAccount(email);
        UsernameNotFoundException e = assertThrows(UsernameNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_ACCOUNT_FOR_EMAIL.getCode(), e.getMessage());
    }

    @Test
    @DisplayName("확인 코드 검증: Success")
    void verifyIdentificationCode_WhenSuccess_ReturnVerifyResponse() {
        // Given
        String email = "testgardener@notagardener.xyz";
        String identificationCode = "IDENTIFICATION_CODE";
        VerifyRequest verifyRequest = new VerifyRequest(email, identificationCode);
        LostGardener lostGardener = new LostGardener(identificationCode, email, getMockedUsernames());
        when(lostGardenerRepository.findById(identificationCode)).thenReturn(Optional.of(lostGardener));

        // When
        VerifyResponse result = forgotService.verifyIdentificationCode(verifyRequest);

        // Then
        assertNotNull(result);
        assertTrue(result.getVerified());
        assertEquals("본인 확인 코드", result.getSubject());
    }

    @Test
    @DisplayName("확인 코드 검증: 레디스에 해당 정보 없음 - 확인 코드(PK) 오류")
    void verifyIdentificationCode_WhenIdentificationCodeInvalid_ThrowsVerificationException() {
        // Given
        String email = "testgardener@notagardener.xyz";
        String identificationCode = "IDENTIFICATION_CODE";
        VerifyRequest verifyRequest = new VerifyRequest(email, identificationCode);

        when(lostGardenerRepository.findById(identificationCode)).thenReturn(Optional.empty());

        // When, Then
        Executable executable = () -> forgotService.verifyIdentificationCode(verifyRequest);
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, executable);
        assertEquals(ExceptionCode.NO_IDENTIFICATION_INFO_IN_REDIS, e.getCode());
    }

    @Test
    @DisplayName("확인 코드 검증: 확인 코드가 본인 것이 아님 - 이메일 오류")
    void verifyIdentificationCode_WhenEmailInvalid_ThrowsVerificationException() {
        // Given
        String invalidEmail = "notyourcode@notagardener.xyz";
        String validEmail = "thatsmycode@notagardener.xyz";
        String identificationCode = "IDENTIFICATION_CODE";
        VerifyRequest verifyRequest = new VerifyRequest(invalidEmail, identificationCode);
        LostGardener lostGardener = new LostGardener(identificationCode, validEmail, getMockedUsernames());

        when(lostGardenerRepository.findById(identificationCode)).thenReturn(Optional.of(lostGardener));

        // When, Then
        Executable executable = () -> forgotService.verifyIdentificationCode(verifyRequest);
        VerificationException e = assertThrows(VerificationException.class, executable);
        assertEquals(ExceptionCode.NOT_YOUR_IDENTIFICATION_CODE, e.getCode());
    }

    @Test
    @DisplayName("비밀번호 재설정: Happy Path")
    void resetPassword_WhenLoginValid_ShouldChangePassword() {
        // Given
        String username = "testgardener";
        String password = "newPassword";
        String encryptedPassword = "encryptedPassword";

        Login login = new Login(username, password);
        Gardener existingGardener = mock(Gardener.class);

        when(gardenerRepository.findByProviderIsNullAndUsername(username)).thenReturn(Optional.of(existingGardener));
        when(encoder.encode(password)).thenReturn(encryptedPassword);

        // When
        forgotService.resetPassword(login);

        // Then
        verify(existingGardener, times(1)).changePassword(encryptedPassword);
    }

    @Test
    @DisplayName("비밀번호 재설정: 해당 username 없음")
    void resetPassword_WhenInvalidUsername_ThrowException() {
        // Given
        String username = "nonexistgardener";
        String password = "newPassword";

        Login login = new Login(username, password);

        when(gardenerRepository.findByProviderIsNullAndUsername(username)).thenReturn(Optional.empty());

        // When, Then
        assertThrows(UsernameNotFoundException.class, () -> forgotService.resetPassword(login));
    }
}