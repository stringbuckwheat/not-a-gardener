package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GardenerDto;

public interface AuthenticationService {
    /* 아이디 중복 검사 */
    String hasSameUsername(String username);

    /* 회원 가입 */
    GardenerDto.Info add(GardenerDto.Register register);

    /* 로그인 */
    GardenerDto.Info login(GardenerDto.Login login);

    /* access token 갱신 */
    GardenerDto.Token refreshAccessToken(GardenerDto.Refresh token);

    /* 회원 정보: 헤더 및 localStorage 저장용 */
    GardenerDto.Info getGardenerInfo(Long id);
}
