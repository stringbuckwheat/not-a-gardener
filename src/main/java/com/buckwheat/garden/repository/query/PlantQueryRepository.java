package com.buckwheat.garden.repository.query;

import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.projection.RawGarden;
import com.buckwheat.garden.repository.query.querydsl.PlantRepositoryCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlantQueryRepository extends Repository<Plant, Long>, PlantRepositoryCustom {
    List<Plant> findByGardener_GardenerIdOrderByPlantIdDesc(Long gardenerId);

    Optional<Plant> findByPlantIdAndGardener_GardenerId(Long plantId, Long gardenerId);

    // 메인페이지 todolist 용 메소드
    @Query(value = "SELECT p.plant_id plantId, p.name name, p.species, p.recent_watering_period recentWateringPeriod, " +
            " p.early_watering_period earlyWateringPeriod, p.medium, p.birthday, p.condition_date conditionDate, " +
            " p.create_date createDate, max(watering_date) latestWateringDate, p.postpone_date postponeDate, " +
            " pl.place_id placeId, pl.name placeName" +
            " FROM plant p" +
            " INNER JOIN place pl" +
            " ON p.place_id = pl.place_id" +
            " INNER JOIN watering w" +
            " ON p.plant_id = w.plant_id" +
            " WHERE p.gardener_id = :gardenerId" +
            " AND (DATE_FORMAT(p.condition_date, '%Y-%m-%d') != CURDATE() OR condition_date IS NULL)" +
            " AND p.recent_watering_period != 0 " +
            " GROUP BY p.plant_id" +
            " HAVING MAX(watering_date) != CURDATE()" +
            " AND DATEDIFF(CURDATE(), MAX(watering_date)) >= p.recent_watering_period - 1" +
            " ORDER BY p.recent_watering_period DESC", nativeQuery = true)
    List<RawGarden> findGardenByGardenerId(@Param("gardenerId") Long gardenerId);
}
