package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WateringRepository extends JpaRepository<Watering, Integer> {
    @Query(value = "SELECT MAX(watering_date)"
            + " FROM watering w "
            + " WHERE w.plant_no = :plantNo"
            + " AND w.fertilized = 'Y'", nativeQuery = true)
    LocalDate findLatestFertilizedDayByPlantNo(@Param("plantNo") int plantNo);

    @Query(value = "SELECT MAX(watering_date)"
            + " FROM watering w "
            + " WHERE w.plant_no = :plantNo"
            + " AND w.fertilized != 'Y'", nativeQuery = true)
    LocalDate findLatestWateringDayByPlantNo(@Param("plantNo") int plantNo);

    // Watering findTopByChemical_ChemicalNoIsNotNullAndPlant_PlantNoByWateringDateDesc(int plantNo);

//    // 쿼리 메소드 테스트
//    Watering findTopByPlantNoAndFertilizedOrderByWateringNoDesc(int plantNo, String y);
//    Watering findTopByPlantNoAndFertilizedNotOrderByWateringNoDesc(int plantNo, String y);

    // Top == limit
    // ORDER BY Watering_no DESC
    /* 가장 최근 물 준 날짜 두 개를 들고 옴 */
    List<Watering> findTop2ByPlant_PlantNoOrderByWateringNoDesc(int plantNo);

    @EntityGraph(attributePaths = {"chemical"}, type= EntityGraph.EntityGraphType.FETCH)
    List<Watering> findByPlant_plantNoOrderByWateringDateDesc(int plantNo);
}
