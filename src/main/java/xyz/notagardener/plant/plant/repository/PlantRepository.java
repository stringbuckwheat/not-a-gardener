package xyz.notagardener.plant.plant.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import xyz.notagardener.common.validation.YesOrNoType;
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

    @EntityGraph(attributePaths = {"status"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Plant> findByGardener_GardenerIdAndStatus_Attention(Long gardenerId, YesOrNoType active);

    @Query("""
            SELECT 
                new xyz.notagardener.plant.garden.dto.PlantResponse(
                    p, pl, w.wateringId, max(w.wateringDate), count(w), ps
                ) 
            FROM Plant p 
            JOIN p.place pl 
            JOIN p.waterings w 
            LEFT JOIN p.status ps
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

    void delete(Plant plant);
}
