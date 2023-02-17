package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.token.PasswordAuthenticationToken;

public interface LoginService {
    /* 로그인 후 토큰 발급 */
    String login(MemberDto memberDto);

    /* 토큰 생성 */
    String createToken(PasswordAuthenticationToken token);
}
