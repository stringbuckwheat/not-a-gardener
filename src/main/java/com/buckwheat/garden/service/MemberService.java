package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.MemberDto;
import com.buckwheat.garden.data.entity.Member;

public interface MemberService {
    // 회원 정보 보기
    MemberDto.MemberDetail getMember(Member member);

    // 회원 정보 변경
    MemberDto.MemberDetail updateMember(MemberDto.MemberDetail memberDetailDto);

    // id, pw으로 본인 확인
    boolean identifyMember(MemberDto.Login loginDto, Member member);

    // 비밀번호 변경
    void updatePassword(MemberDto.Login memberDto, Member member);

    // 회원 탈퇴
    void removeMember(int memberNo);
}
