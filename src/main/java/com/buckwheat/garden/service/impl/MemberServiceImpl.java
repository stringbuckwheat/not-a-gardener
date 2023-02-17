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
    public MemberDetailDto getMember(String username) {
        Member member = memberRepository.findById(username).orElseThrow(NoSuchElementException::new);
        return new MemberDetailDto(member.getUsername(), member.getEmail(), member.getName());
    }

    @Override
    public void updatePassword(MemberDto memberDto) {
        Member member = memberRepository.findById(memberDto.getUsername()).orElseThrow(NoSuchElementException::new);
        member.changePassword(encoder.encode(memberDto.getPw()));
    }

    @Override
    public boolean identifyMember(MemberDto memberDto){
        return memberRepository.findByUsernameAndPw(memberDto.getUsername(), memberDto.getPw()).isPresent();
    }

    @Override
    public RegisterDto updateMember(RegisterDto registerDto){
        memberRepository.save(registerDto.toEntity());
        return registerDto;
    }

    @Override
    public void removeMember(String username) {
        memberRepository.deleteById(username);
    }
}
