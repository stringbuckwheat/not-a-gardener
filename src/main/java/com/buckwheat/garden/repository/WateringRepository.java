package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.dto.ChemicalUsage;
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
public interface WateringRepository extends JpaRepository<Watering, Long> {
    @Query(value = "SELECT watering, chemical, plant" +
            " FROM Watering watering" +
            " LEFT JOIN Chemical chemical" +
            " ON watering.chemical = chemical" +
            " JOIN Plant plant" +
            " ON watering.plant = plant" +
            " WHERE plant.member.memberId = :memberId" +
            " AND watering.wateringDate >= :startDate" +
            " AND watering.wateringDate <= :endDate")
    List<Watering> findAllWateringListByMemberNo(@Param("memberId") Long memberId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "select chemical_no chemicalNo, chemical_period chemicalPeriod, chemical_name chemicalName," +
            " (select MAX(watering_date) from watering w where w.plant_no = :plantId and w.chemical_no = c.chemical_no) latestWateringDate" +
            " from chemical c where c.member_no = :memberId" +
            " order by chemical_period desc", nativeQuery = true)
    List<ChemicalUsage> findLatestChemicalizedDayList(@Param("memberId") Long memberId, @Param("plantId") Long plantId);

    @EntityGraph(attributePaths = {"chemical"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Watering> findByPlant_PlantIdOrderByWateringDateDesc(Long plantId);

    @EntityGraph(attributePaths = {"chemical, plant"}, type = EntityGraph.EntityGraphType.FETCH)
    Watering findByWateringId(Long wateringId);

    @Transactional
    void deleteAllByPlant_PlantId(Long plantId);
}
