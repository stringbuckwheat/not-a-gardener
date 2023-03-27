package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Watering;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WateringRepository extends JpaRepository<Watering, Integer> {
    @Query(value = "SELECT MAX(watering_date) from watering w where w.plant_no = :plantNo AND chemical_no = :chemicalNo", nativeQuery = true)
    LocalDate findLatestFertilizingDayByPlantNoAndChemicalNo(@Param("plantNo") int plantNo, @Param("chemicalNo") int chemicalNo);

    @EntityGraph(attributePaths = {"chemical"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Watering> findByPlant_plantNoOrderByWateringDateDesc(int plantNo);

    @EntityGraph(attributePaths = {"chemical, plant"}, type = EntityGraph.EntityGraphType.FETCH)
    Watering findByWateringNo(int wateringNo);

    @Transactional
    void deleteAllByPlant_plantNo(int plantNo);
}
