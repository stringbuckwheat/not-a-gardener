package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.RegisterDto;

public interface RegisterService {
    /* 아이디 중복 검사 */
    String getIdByInputId(String id);

    /* email 중복 검사 */
    String getEmailByInputEmail(String email);

    /* 회원 가입 */
    RegisterDto addMember(RegisterDto paramRegisterDto);
}
