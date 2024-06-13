package xyz.notagardener.watering.watering.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import xyz.notagardener.watering.Watering;

import java.util.Optional;

public interface WateringRepository extends Repository<Watering, Long>, WateringRepositoryCustom {
    Optional<Watering> findByWateringIdAndPlant_Gardener_GardenerId(Long wateringId, Long gardenerId);

    @EntityGraph(attributePaths = {"plant", "plant.gardener"})
    Optional<Watering> findByWateringIdAndPlant_PlantId(Long wateringId, Long plantId);
    Watering save(Watering watering);
    void delete(Watering watering);
    void deleteAllByPlant_PlantId(Long plantId);
}