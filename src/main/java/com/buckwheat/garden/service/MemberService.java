package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.entity.Member;

import java.util.Map;

public interface MemberService {
    // 회원 정보 보기
    MemberDto.Detail getMemberDetail(Member member);

    // 회원 정보 변경
    MemberDto.Detail modify(MemberDto.Detail memberDetail);

    // 본인 확인 코드 리턴 및 본인 확인 메일 전송(아이디 찾기)
    Map<String, Object> forgotAccount(String email);

    // id, pw으로 본인 확인
    boolean identify(Member member, MemberDto.Login login);

    // 회원 정보에서 비밀번호 변경
    void updatePassword(Member member, MemberDto.Login login);

    // 비밀번호 찾기에서 비밀번호 변경
    void resetPassword(MemberDto.Login login);

    // 회원 탈퇴
    void delete(Long memberId);
}
