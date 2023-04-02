package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.entity.Member;

import java.util.Map;

public interface MemberService {
    // 회원 정보 보기
    MemberDto.MemberDetail getMember(Member member);

    // 회원 정보 변경
    MemberDto.MemberDetail updateMember(MemberDto.MemberDetail memberDetailDto);

    // 본인 확인 코드 리턴 및 본인 확인 메일 전송(아이디 찾기)
    Map<String, Object> getIdentificationCodeAndMembers(String email);

    // id, pw으로 본인 확인
    boolean identifyMember(MemberDto.Login login, Member member);

    // 회원 정보에서 비밀번호 변경
    void updatePassword(MemberDto.Login login, Member member);

    // 비밀번호 찾기에서 비밀번호 변경
    void resetPassword(MemberDto.Login login);

    // 회원 탈퇴
    void removeMember(int memberNo);
}
