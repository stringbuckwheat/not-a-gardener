package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.GardenerDto;
import com.buckwheat.garden.data.entity.Gardener;

import java.util.Map;

public interface GardenerService {
    // 회원 정보 보기
    GardenerDto.Detail getGardenerDetail(Gardener gardener);

    // 회원 정보 변경
    GardenerDto.Detail modify(GardenerDto.Detail gardenerDetail);

    // 본인 확인 코드 리턴 및 본인 확인 메일 전송(아이디 찾기)
    Map<String, Object> forgotAccount(String email);

    // id, pw으로 본인 확인
    boolean identify(Gardener gardener, GardenerDto.Login login);

    // 회원 정보에서 비밀번호 변경
    void updatePassword(Gardener gardener, GardenerDto.Login login);

    // 비밀번호 찾기에서 비밀번호 변경
    void resetPassword(GardenerDto.Login login);

    // 회원 탈퇴
    void delete(Long gardenerId);
}
