package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Fertilizer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FertilizerRepository extends JpaRepository<Fertilizer, Integer> {
}
