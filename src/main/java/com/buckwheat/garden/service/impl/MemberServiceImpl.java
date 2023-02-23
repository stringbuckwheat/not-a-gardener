package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.MemberDetailDto;
import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.dto.RegisterDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.repository.MemberRepository;
import com.buckwheat.garden.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
    private final BCryptPasswordEncoder encoder;
    private final MemberRepository memberRepository;

    @Override
    public MemberDetailDto getMember(int userNo) {
        Member member = memberRepository.findById(userNo).orElseThrow(NoSuchElementException::new);

        return MemberDetailDto.builder()
                .username(member.getUsername())
                .email(member.getEmail())
                .name(member.getName())
                .createDate(member.getCreateDate())
                .provider(member.getProvider())
                .build();
    }

    @Override
    public void updatePassword(MemberDto memberDto) {
//        Member member = memberRepository.findById(memberDto.getUsername()).orElseThrow(NoSuchElementException::new);
//        member.changePassword(encoder.encode(memberDto.getPw()));
    }

    @Override
    public boolean identifyMember(MemberDto memberDto){
        return memberRepository.findByUsernameAndPw(memberDto.getUsername(), memberDto.getPw()).isPresent();
    }

    @Override
    public MemberDetailDto updateMember(MemberDetailDto memberDetailDto){
        Member member = memberRepository.findById(memberDetailDto.getMemberNo()).orElseThrow(NoSuchElementException::new);

        member.updateEmailAndName(memberDetailDto.getEmail(), memberDetailDto.getName());
        memberRepository.save(member);

        return MemberDetailDto.builder()
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
