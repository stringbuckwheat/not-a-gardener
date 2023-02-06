package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.PlantStatusInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantStatusInfoRepository extends JpaRepository<PlantStatusInfo, Integer> {
}
