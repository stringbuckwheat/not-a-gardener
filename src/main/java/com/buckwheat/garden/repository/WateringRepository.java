package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

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
            + " AND w.fertilized IS NULL", nativeQuery = true)
    LocalDate findLatestWateringDayByPlantNo(@Param("plantNo") int plantNo);
}
