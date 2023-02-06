package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.PesticideInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PesticideInfoRepository extends JpaRepository<PesticideInfo, Integer> {
}
