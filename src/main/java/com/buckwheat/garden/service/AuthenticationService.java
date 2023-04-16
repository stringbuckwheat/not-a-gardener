package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.MemberDto;

public interface AuthenticationService {
    /* 아이디 중복 검사 */
    String hasSameUsername(String username);

    /* 회원 가입 */
    MemberDto.Info addMember(MemberDto.Register paramRegisterDto);

    /* 로그인 */
    MemberDto.Info login(MemberDto.Login login);
}
