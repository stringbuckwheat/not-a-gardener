package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.RegisterDto;
import com.buckwheat.garden.data.entity.Member;
import com.buckwheat.garden.repository.MemberRepository;
import com.buckwheat.garden.service.RegisterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @Override
    public RegisterDto addMember(RegisterDto paramRegisterDto) {
        log.debug("addMember(): " + paramRegisterDto);

        // DTO에 암호화된 비밀번호 저장하기
        paramRegisterDto.encryptPassword(encoder.encode(paramRegisterDto.getPw()));

        memberRepository.save(paramRegisterDto.toEntity());
        return paramRegisterDto;
    }

    @Override
    public String getIdByInputId(String id) {
        Optional<Member> member = memberRepository.findByUsername(id);

        if(member.isEmpty()){
            return null;
        }

        return member.get().getUsername();
    }
}
