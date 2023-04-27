package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberDao {
    Optional<Member> getMemberByUsername(String username);
    Member getMemberForLogin(String username);
    Optional<Member> getMemberByMemberId(Long id);
    List<Member> getMemberByEmail(String email);
    Optional<Member> getMemberByUsernameAndProvider(String email, String provider);
    Member save(Member member);
    void deleteBy(Long id);
}
