package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Fertilizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FertilizerRepository extends JpaRepository<Fertilizer, Integer> {
    List<Fertilizer> findByMember_memberNo(int memberNo);
}
