package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.MemberDao;
import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.service.MemberService;
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
public class MemberServiceImpl implements MemberService {
    private final BCryptPasswordEncoder encoder;
    private final MemberDao memberDao;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Override
    public MemberDto.Detail getMemberDetail(Member member) {
        return MemberDto.Detail.from(member);
    }

    /**
     * 계정 찾기
     *
     * @param email
     * @return
     */
    @Override
    public Map<String, Object> forgotAccount(String email) {
        List<Member> memberList = memberDao.getMemberByEmail(email);

        if (memberList.size() == 0) {
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

        List<String> members = new ArrayList<>();

        for (Member member : memberList) {
            members.add(member.getUsername());
        }

        map.put("members", members);

        return map;
    }

    @Override
    public void updatePassword(Member member, MemberDto.Login login) {
        String encryptedPassword = encoder.encode(login.getPassword());
        memberDao.save(member.changePassword(encryptedPassword));
    }

    @Override
    public void resetPassword(MemberDto.Login login) {
        Member member = memberDao.getMemberByUsername(login.getUsername()).orElseThrow(NoSuchElementException::new);
        updatePassword(member, login);
    }

    @Override
    public boolean identify(Member member, MemberDto.Login login) {
        return encoder.matches(login.getPassword(), member.getPassword());
    }

    @Override
    public MemberDto.Detail modify(MemberDto.Detail memberDetail) {
        Member member = memberDao.getMemberByMemberId(memberDetail.getId()).orElseThrow(NoSuchElementException::new);
        memberDao.save(member.updateEmailAndName(memberDetail.getEmail(), memberDetail.getName()));

        return MemberDto.Detail.updateResponseFrom(member);
    }

    @Override
    public void delete(Long memberId) {
        memberDao.deleteBy(memberId);
    }
}
