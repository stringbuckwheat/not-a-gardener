package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.RegisterDto;

public interface RegisterService {
    /* 아이디 중복 검사 */
    String getIdByInputId(String id);

    /* 회원 가입 */
    RegisterDto addMember(RegisterDto paramRegisterDto);
}
