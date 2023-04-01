package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
    Optional<Member> findByUsername(String username);
    Optional<Member> findByUsernameAndProvider(String username, String registrationId);

    List<Member> findByEmailAndProviderIsNull(String email);
}
