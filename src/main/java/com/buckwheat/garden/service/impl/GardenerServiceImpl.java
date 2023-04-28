package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.GardenerDao;
import com.buckwheat.garden.data.dto.GardenerDto;
import com.buckwheat.garden.data.entity.Gardener;
import com.buckwheat.garden.service.GardenerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class GardenerServiceImpl implements GardenerService {
    private final BCryptPasswordEncoder encoder;
    private final GardenerDao gardenerDao;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Override
    public GardenerDto.Detail getGardenerDetail(Gardener gardener) {
        return GardenerDto.Detail.from(gardener);
    }

    /**
     * 계정 찾기
     *
     * @param email
     * @return
     */
    @Override
    public Map<String, Object> forgotAccount(String email) {
        List<Gardener> gardeners = gardenerDao.getGardenerByEmail(email);

        if (gardeners.size() == 0) {
            throw new UsernameNotFoundException("해당 이메일로 가입한 회원이 없어요.");
        }

        // 본인확인 코드 만들기
        String identificationCode = RandomStringUtils.randomAlphanumeric(6);

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

        // 리턴값 만들기
        Map<String, Object> map = new HashMap<>();
        map.put("identificationCode", identificationCode);
        map.put("email", email);

        List<String> gardenerDtos = new ArrayList<>();

        for (Gardener gardener : gardeners) {
            gardenerDtos.add(gardener.getUsername());
        }

        map.put("gardeners", gardenerDtos);

        return map;
    }

    @Override
    public void updatePassword(Gardener gardener, GardenerDto.Login login) {
        String encryptedPassword = encoder.encode(login.getPassword());
        gardenerDao.save(gardener.changePassword(encryptedPassword));
    }

    @Override
    public void resetPassword(GardenerDto.Login login) {
        Gardener gardener = gardenerDao.getGardenerByUsername(login.getUsername()).orElseThrow(NoSuchElementException::new);
        updatePassword(gardener, login);
    }

    @Override
    public boolean identify(Gardener gardener, GardenerDto.Login login) {
        return encoder.matches(login.getPassword(), gardener.getPassword());
    }

    @Override
    public GardenerDto.Detail modify(GardenerDto.Detail gardenerDetail) {
        Gardener gardener = gardenerDao.getGardenerByGardenerId(gardenerDetail.getId())
                .orElseThrow(NoSuchElementException::new);
        gardenerDao.save(gardener.updateEmailAndName(gardenerDetail.getEmail(), gardenerDetail.getName()));

        return GardenerDto.Detail.updateResponseFrom(gardener);
    }

    @Override
    public void delete(Long gardenerId) {
        gardenerDao.deleteBy(gardenerId);
    }
}
