package xyz.notagardener.gardener.forgot;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.gardener.Gardener;
import xyz.notagardener.gardener.authentication.dto.Login;
import xyz.notagardener.gardener.gardener.GardenerRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForgotServiceImpl implements ForgotService {
    private final GardenerRepository gardenerRepository;
    private final LostGardenerRepository lostGardenerRepository;
    private final JavaMailSender mailSender;
    private final BCryptPasswordEncoder encoder;

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Override
    @Transactional(readOnly = true)
    public Forgot forgotAccount(String email) {
        List<Username> usernames = gardenerRepository.findByProviderIsNullAndEmail(email);

        if (usernames.size() == 0) {
            throw new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT_FOR_EMAIL.getCode());
        }

        // 본인확인 코드 만들기
        String identificationCode = RandomStringUtils.randomAlphanumeric(6);
        sendEmail(identificationCode, email); // 이메일 보내기

        // Redis 저장
        LostGardener lostGardener = new LostGardener(identificationCode, email, usernames);

        return new Forgot(email, usernames);
    }

    private void sendEmail(String identificationCode, String email) {
        // 메일 내용 만들기
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("본인 확인 코드는 [ ")
                .append(identificationCode)
                .append(" ] 입니다.");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(sendFrom);
        message.setSubject("[not-a-gardner] 본인확인 코드가 도착했어요.");
        message.setText(stringBuilder.toString());

        mailSender.send(message);
    }

    @Override
    public VerifyRequest verifyIdentificationCode(VerifyRequest verifyRequest) {
        LostGardener lostGardener = lostGardenerRepository.findById(verifyRequest.getIdentificationCode())
                .orElseThrow(() -> new BadCredentialsException(ExceptionCode.NO_IDENTIFICATION_INFO_IN_REDIS.getCode()));

        if(!verifyRequest.getEmail().equals(lostGardener.getEmail())) {
            throw new BadCredentialsException(ExceptionCode.NOT_YOUR_IDENTIFICATION_CODE.getCode());
        }

        lostGardenerRepository.deleteById(lostGardener.getIdentificationCode());

        return verifyRequest;
    }

    @Override
    @Transactional
    public void resetPassword(Login login) {
        Gardener gardener = gardenerRepository.findByProviderIsNullAndUsername(login.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT.getCode()));

        String encryptedPassword = encoder.encode(login.getPassword());
        gardener.changePassword(encryptedPassword);
    }
}
