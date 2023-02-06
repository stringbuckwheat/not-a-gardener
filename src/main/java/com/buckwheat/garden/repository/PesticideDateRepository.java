package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.PesticideDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PesticideDateRepository extends JpaRepository<PesticideDate, Integer> {
}
