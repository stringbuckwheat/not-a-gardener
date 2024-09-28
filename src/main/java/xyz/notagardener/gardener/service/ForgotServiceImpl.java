package xyz.notagardener.gardener.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.error.code.ExceptionCode;
import xyz.notagardener.common.error.exception.ResourceNotFoundException;
import xyz.notagardener.common.error.exception.VerificationException;
import xyz.notagardener.gardener.model.Gardener;
import xyz.notagardener.authentication.dto.Login;
import xyz.notagardener.gardener.dto.*;
import xyz.notagardener.gardener.model.LostGardener;
import xyz.notagardener.gardener.repository.GardenerRepository;
import xyz.notagardener.gardener.repository.LostGardenerRepository;

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
        List<Username> usernames = getUsernameByEmail(email);

        // 본인확인 코드 만들기
        String identificationCode = RandomStringUtils.randomAlphanumeric(6);
        log.debug("identificationCode: {}", identificationCode);
//        sendEmail(identificationCode, email); // 이메일 보내기

        // Redis 저장
        lostGardenerRepository.save(new LostGardener(identificationCode, email, usernames));

        return new Forgot(email, usernames);
    }

    @Override
    public List<Username> getUsernameByEmail(String email) {
        List<Username> usernames = gardenerRepository.findByProviderIsNullAndEmail(email);

        if (usernames.isEmpty()) {
            throw new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT_FOR_EMAIL.getCode());
        }

        return usernames;
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
    public VerifyResponse verifyIdentificationCode(VerifyRequest verifyRequest) {
        // 본인 여부 확인
        LostGardener lostGardener = lostGardenerRepository.findById(verifyRequest.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException(ExceptionCode.NO_IDENTIFICATION_INFO_IN_REDIS));

        // 본인 확인 코드 확인
        if(!verifyRequest.getIdentificationCode().equals(lostGardener.getIdentificationCode())) {
            throw new VerificationException(ExceptionCode.NOT_YOUR_IDENTIFICATION_CODE);
        }

        // redis에서 확인코드 삭제
        lostGardenerRepository.deleteById(lostGardener.getEmail());

        return new VerifyResponse(true);
    }

    @Override
    @Transactional
    public void resetPassword(Login login) {
        Gardener gardener = gardenerRepository.findByProviderIsNullAndUsername(login.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionCode.NO_ACCOUNT.getCode()));

        // 새 비밀번호 인코딩 후 저장
        gardener.changePassword(encoder.encode(login.getPassword()));
    }
}
