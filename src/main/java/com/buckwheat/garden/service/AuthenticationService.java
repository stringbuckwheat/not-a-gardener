package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.MemberDto;

public interface AuthenticationService {
    /* 아이디 중복 검사 */
    String getIdByInputId(String id);

    /* 회원 가입 */
    MemberDto.MemberInfo addMember(MemberDto.RegisterDto paramRegisterDto);

    /* 로그인 */
    MemberDto.MemberInfo login(MemberDto.Login login);
}
