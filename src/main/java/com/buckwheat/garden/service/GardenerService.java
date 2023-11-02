package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.gardener.GardenerDetail;
import com.buckwheat.garden.data.dto.gardener.Login;

public interface GardenerService {
    // 회원 정보 보기
    GardenerDetail getOne(Long id);

    // id, pw으로 본인 확인
    boolean identify(Long id, Login login);

    // 회원 정보 변경
    GardenerDetail update(GardenerDetail gardenerDetail);

    // 회원 정보에서 비밀번호 변경
    void updatePassword(Long id, Login login);

    // 회원 탈퇴
    void delete(Long gardenerId);
}
