package xyz.notagardener.watering.watering.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.Repository;
import xyz.notagardener.watering.Watering;

import java.time.LocalDate;
import java.util.Optional;

public interface WateringRepository extends Repository<Watering, Long>, WateringRepositoryCustom {
    Optional<Watering> findById(Long wateringId);
    Optional<Watering> findByWateringIdAndPlant_Gardener_GardenerId(Long wateringId, Long gardenerId);

    @EntityGraph(attributePaths = {"plant", "plant.gardener"})
    Optional<Watering> findByWateringIdAndPlant_PlantId(Long wateringId, Long plantId);
    @EntityGraph(attributePaths = {"chemical"})
    Optional<Watering> findByWateringDateAndPlant_PlantId(LocalDate wateringDate, Long plantId);
    Optional<Watering> findByWateringDateAndPlant_Gardener_GardenerId(LocalDate wateringDate, Long plantId);
    Watering save(Watering watering);
    void deleteById(Long wateringId);
    void delete(Watering watering);
    void deleteAllByPlant_PlantId(Long plantId);

    void deleteAllByWateringDateAndPlant_PlantId(LocalDate wateringDate, Long plantId);
}