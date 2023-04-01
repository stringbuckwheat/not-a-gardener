package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.repository.MemberRepository;
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
    private final MemberRepository memberRepository;

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String sendFrom;

    @Override
    public MemberDto.MemberDetail getMember(Member member) {
        return MemberDto.MemberDetail.from(member);
    }

    @Override
    public Map<String, Object> getIdentificationCodeAndMembers(String email) {
        log.debug("email: {}", email);
        List<Member> memberList = memberRepository.findByEmailAndProviderIsNull(email);

        if(memberList.size() == 0){
            throw new UsernameNotFoundException("해당 이메일로 가입한 회원이 없어요.");
        }

        // 본인확인 코드 만들기
        String identificationCode = RandomStringUtils.randomAlphanumeric(6);
        log.debug("identificationCode: {}", identificationCode);

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

        for(Member member : memberList){
            members.add(member.getUsername());
        }

        map.put("members", members);

        return map;
    }

    @Override
    public void updatePassword(MemberDto.Login login, Member member) {
        String encryptedPassword = encoder.encode(login.getPw());
        memberRepository.save(member.changePassword(encryptedPassword));
    }

    @Override
    public boolean identifyMember(MemberDto.Login loginDto, Member member){
        return encoder.matches(loginDto.getPw(), member.getPw());
    }

    @Override
    public MemberDto.MemberDetail updateMember(MemberDto.MemberDetail memberDetailDto){
        Member member = memberRepository.findById(memberDetailDto.getMemberNo()).orElseThrow(NoSuchElementException::new);

        member.updateEmailAndName(memberDetailDto.getEmail(), memberDetailDto.getName());
        memberRepository.save(member);

        return MemberDto.MemberDetail.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .createDate(member.getCreateDate())
                .build();
    }

    @Override
    public void removeMember(int memberNo) {
        memberRepository.deleteById(memberNo);
    }
}
