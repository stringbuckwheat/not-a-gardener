package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.MemberDetailDto;
import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.dto.RegisterDto;
import com.buckwheat.garden.data.token.PasswordAuthenticationToken;

public interface MemberService {
    // 회원 정보 보기
    MemberDetailDto getMember(String username);

    // 회원 정보 변경
    RegisterDto updateMember(RegisterDto registerDto);

    // id, pw으로 본인 확인
    boolean identifyMember(MemberDto memberDto);

    // 비밀번호 변경
    void updatePassword(MemberDto memberDto);

    // 회원 탈퇴
    void removeMember(String username);
}
