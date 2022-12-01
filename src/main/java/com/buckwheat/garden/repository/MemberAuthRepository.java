package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.MemberAuth;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberAuthRepository extends JpaRepository<MemberAuth, String> {
}
