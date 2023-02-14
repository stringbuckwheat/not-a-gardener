package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.dto.RegisterDto;
import com.buckwheat.garden.data.token.PasswordAuthenticationToken;

public interface LoginService {
    // ID 중복 검사 체크
    String getIdByInputId(String id);

    // 회원 추가
    RegisterDto addMember(RegisterDto registerDto);

    // 로그인
    String login(MemberDto memberDto);

    // 토큰 생성
    String createToken(PasswordAuthenticationToken token);

    // email 중복 검사
    String getEmailByInputEmail(String email);
}
