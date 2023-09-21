package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.gardener.*;

public interface AuthenticationService {
    /* 아이디 중복 검사 */
    String hasSameUsername(String username);

    /* 회원 가입 */
    Info add(Register register);

    /* 로그인 */
    Info login(Login login);

    // 로그아웃
    void logOut(Long id);

    /* access token 갱신 */
    Token refreshAccessToken(Refresh token);

    /* 회원 정보: 헤더 및 localStorage 저장용 */
    Info getGardenerInfo(Long id);
}
