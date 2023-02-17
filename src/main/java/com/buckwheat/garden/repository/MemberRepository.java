package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Optional<Member> findByEmail(String email);
    Optional<Member> findByUsernameAndPw(String username, String pw);
    Optional<Member> findByUsernameAndProvider(String username, String registrationId);
}
