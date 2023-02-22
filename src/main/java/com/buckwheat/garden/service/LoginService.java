package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.dto.MemberInfo;

public interface LoginService {
    /* 로그인 후 토큰 발급 */
    MemberInfo login(MemberDto memberDto);
}
