package xyz.notagardener.plant.plant.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import xyz.notagardener.plant.Plant;
import xyz.notagardener.plant.garden.dto.PlantResponse;

import java.util.List;
import java.util.Optional;

public interface PlantRepository extends Repository<Plant, Long>, PlantRepositoryCustom {
    Optional<Plant> findById(Long plantId);

    @EntityGraph(attributePaths = {"place", "waterings", "waterings.chemical"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Plant> findByPlantIdAndGardener_GardenerId(Long plantId, Long gardenerId);

    @EntityGraph(attributePaths = {"place", "waterings", "waterings.chemical", "gardener"}, type = EntityGraph.EntityGraphType.FETCH)
    Optional<Plant> findByPlantId(Long plantId);

    List<Plant> findByGardener_GardenerIdOrderByPlantIdDesc(Long gardenerId);

    @Query("""
            SELECT 
                new xyz.notagardener.plant.garden.dto.PlantResponse(
                    p.plantId, p.name, p.species, p.recentWateringPeriod, p.earlyWateringPeriod, 
                    p.medium, p.birthday, p.conditionDate, p.postponeDate, p.createDate, 
                    pl.placeId, pl.name placeName, w.wateringId, 
                    max(w.wateringDate) latestWateringDate, 
                    count(w) totalWatering
                ) 
            FROM Plant p 
            JOIN p.place pl 
            JOIN p.waterings w 
            WHERE p.gardener.gardenerId = :gardenerId 
            AND (DATE_FORMAT(p.conditionDate, '%Y-%m-%d') != CURRENT_DATE OR p.conditionDate IS NULL) 
            AND p.recentWateringPeriod != 0 
            GROUP BY p.plantId 
            HAVING max(w.wateringDate) != CURRENT_DATE 
            AND DATEDIFF(CURRENT_DATE, max(w.wateringDate)) >= p.recentWateringPeriod - 1 
            ORDER BY p.recentWateringPeriod DESC
            """)
    List<PlantResponse> findGardenByGardenerId(@Param("gardenerId") Long gardenerId);

    Plant save(Plant plant);

    void deleteById(Long plantId);

    void delete(Plant plant);
}
