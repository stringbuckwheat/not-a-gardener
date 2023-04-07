package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.dto.FertilizingInfo;
import com.buckwheat.garden.data.entity.Watering;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WateringRepository extends JpaRepository<Watering, Integer> {

    @Query(value = "select chemical_no chemicalNo, chemical_period chemicalPeriod, chemical_name chemicalName," +
            " (select MAX(watering_date) from watering w where w.plant_no = :plantNo and w.chemical_no = c.chemical_no) latestWateringDate" +
            " from chemical c where c.member_no = :memberNo", nativeQuery = true)
    List<FertilizingInfo> findLatestChemicalizedDayList(@Param("plantNo") int plantNo, @Param("memberNo") int memberNo);

    @EntityGraph(attributePaths = {"chemical"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Watering> findByPlant_plantNoOrderByWateringDateDesc(int plantNo);

    @EntityGraph(attributePaths = {"chemical, plant"}, type = EntityGraph.EntityGraphType.FETCH)
    Watering findByWateringNo(int wateringNo);

    @Transactional
    void deleteAllByPlant_plantNo(int plantNo);
}
