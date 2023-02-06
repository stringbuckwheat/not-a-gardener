package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Repot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepotRepository extends JpaRepository<Repot, Integer> {
}
