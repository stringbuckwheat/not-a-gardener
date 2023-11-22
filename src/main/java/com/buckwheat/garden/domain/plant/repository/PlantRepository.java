package com.buckwheat.garden.domain.plant.repository;

import com.buckwheat.garden.domain.plant.Plant;
import com.buckwheat.garden.domain.plant.dto.projection.RawGarden;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlantRepository extends Repository<Plant, Long>, PlantRepositoryCustom {
    Optional<Plant> findById(Long plantId);

    @EntityGraph(attributePaths = {"place", "waterings", "waterings.chemical"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Plant> findByPlantIdAndGardener_GardenerId(Long plantId, Long gardenerId);

    @EntityGraph(attributePaths = {"place", "waterings", "waterings.chemical"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Plant> findByPlantId(Long plantId);

    List<Plant> findByGardener_GardenerIdOrderByPlantIdDesc(Long gardenerId);

    // 메인페이지 todolist 용 메소드
    @Query(value = """
        SELECT
            p.plant_id plantId,
            p.name name,
            p.species,
            p.recent_watering_period recentWateringPeriod,
            p.early_watering_period earlyWateringPeriod,
            p.medium,
            p.birthday,
            p.condition_date conditionDate,
            p.create_date createDate,
            max(watering_date) latestWateringDate,
            p.postpone_date postponeDate,
            pl.place_id placeId,
            pl.name placeName
        FROM
            plant p
        INNER JOIN
            place pl 
        ON 
            p.place_id = pl.place_id
        INNER JOIN
            watering w 
        ON 
            p.plant_id = w.plant_id
        WHERE
            p.gardener_id = :gardenerId
            AND (DATE_FORMAT(p.condition_date, '%Y-%m-%d') != CURDATE() OR condition_date IS NULL)
            AND p.recent_watering_period != 0
        GROUP BY
            p.plant_id
        HAVING
            MAX(watering_date) != CURDATE()
            AND DATEDIFF(CURDATE(), MAX(watering_date)) >= p.recent_watering_period - 1
        ORDER BY
            p.recent_watering_period DESC
        """, nativeQuery = true)
    List<RawGarden> findGardenByGardenerId(@Param("gardenerId") Long gardenerId);

    Plant save(Plant plant);

    void deleteById(Long plantId);
}
