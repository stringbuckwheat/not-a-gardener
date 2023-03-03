package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Pesticide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PesticideRepository extends JpaRepository<Pesticide, Integer> {
    List<Pesticide> findByMember_MemberNo(int memberNo);
}