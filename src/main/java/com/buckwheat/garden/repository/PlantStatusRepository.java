package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.PlantStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlantStatusRepository extends JpaRepository<PlantStatus, Integer> {
}
