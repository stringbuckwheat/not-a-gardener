package com.buckwheat.garden.repository.dao;

import com.buckwheat.garden.data.entity.Plant;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface PlantDao extends Repository<Plant, Long> {
    Optional<Plant> findById(Long plantId);

    @EntityGraph(attributePaths = {"place", "waterings", "waterings.chemical"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Plant> findByPlantIdAndGardener_GardenerId(Long plantId, Long gardenerId);

    @EntityGraph(attributePaths = {"place", "waterings", "waterings.chemical"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Plant> findByPlantId(Long plantId);

    Plant save(Plant plant);

    void deleteById(Long plantId);
}
