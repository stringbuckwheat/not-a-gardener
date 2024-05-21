package xyz.notagardener.gardener.service;

import xyz.notagardener.authentication.dto.Info;
import xyz.notagardener.authentication.dto.Login;
import xyz.notagardener.gardener.dto.GardenerDetail;
import xyz.notagardener.gardener.dto.Register;
import xyz.notagardener.gardener.dto.VerifyResponse;

public interface GardenerService {
    // 회원 정보 보기
    GardenerDetail getOne(Long id);

    // id, pw으로 본인 확인
    VerifyResponse identify(Long id, Login login);

    // 아이디 중복 검사
    String hasSameUsername(String username);

    // 회원가입
    Info add(Register register);

    // 회원 정보 변경
    GardenerDetail update(GardenerDetail gardenerDetail);

    // 회원 정보에서 비밀번호 변경
    void updatePassword(Long id, Login login);

    // 회원 탈퇴
    void delete(Long gardenerId);
}
