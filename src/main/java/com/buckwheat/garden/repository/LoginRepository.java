package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginRepository extends JpaRepository<Member, String> {
}
