package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.entity.Member;

import java.util.Optional;

public interface RegisterDao {
    /* 아이디 중복 검사 */
    Optional<Member> selectIdByInputId(String id);

    /* 회원 가입 */
    Member addMember(Member memberEntity);
}
