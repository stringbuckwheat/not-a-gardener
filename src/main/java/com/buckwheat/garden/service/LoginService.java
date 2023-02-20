package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.MemberDto;

public interface LoginService {
    /* 로그인 후 토큰 발급 */
    String login(MemberDto memberDto);
}
