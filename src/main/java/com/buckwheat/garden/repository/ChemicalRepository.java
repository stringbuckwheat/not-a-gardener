package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Chemical;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChemicalRepository extends JpaRepository<Chemical, Long> {
    List<Chemical> findByGardener_GardenerId(Long gardenerId);

    List<Chemical> findByGardener_GardenerIdOrderByPeriodDesc(Long gardenerId);

    @EntityGraph(attributePaths = {"wateringList", "wateringList.plant", "wateringList.plant.place"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Chemical> findByChemicalId(Long chemicalId);
}
