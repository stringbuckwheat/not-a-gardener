package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.data.projection.ChemicalUsage;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WateringRepository extends JpaRepository<Watering, Long>, WateringRepositoryCustom {
    @EntityGraph(attributePaths = {"chemical"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Watering> findByPlant_PlantIdOrderByWateringDateDesc(Long plantId);

    Watering findByWateringDateAndPlant_PlantId(LocalDate wateringDate, long plantId);

    @Transactional
    void deleteAllByPlant_PlantId(Long plantId);

    @Query(value = "SELECT chemical_id chemicalId, period, name," +
            " (SELECT MAX(watering_date) FROM watering w WHERE w.plant_id = :plantId AND w.chemical_id = c.chemical_id) latestWateringDate" +
            " FROM chemical c WHERE c.gardener_id = :gardenerId" +
            " ORDER BY period DESC", nativeQuery = true)
    List<ChemicalUsage> findLatestChemicalizedDayList(@Param("gardenerId") Long gardenerId, @Param("plantId") Long plantId);
}
