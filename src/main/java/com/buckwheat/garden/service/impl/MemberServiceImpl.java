package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.repository.MemberRepository;
import com.buckwheat.garden.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final BCryptPasswordEncoder encoder;
    private final MemberRepository memberRepository;

    @Override
    public MemberDto.MemberDetail getMember(Member member) {
        return MemberDto.MemberDetail.from(member);
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
