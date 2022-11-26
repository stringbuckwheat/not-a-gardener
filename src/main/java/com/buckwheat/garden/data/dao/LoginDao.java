package com.buckwheat.garden.data.dao;

import com.buckwheat.garden.data.entity.MemberEntity;

import java.util.Optional;

public interface LoginDao {
    /* 아이디 중복 검사 */
    Optional<MemberEntity> selectIdByInputId(String id);

    /* 회원 가입 */
    MemberEntity addMember(MemberEntity memberEntity);
}
