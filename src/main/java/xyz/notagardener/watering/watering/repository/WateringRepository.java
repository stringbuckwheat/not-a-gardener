package xyz.notagardener.watering.watering.repository;

import org.springframework.data.repository.Repository;
import xyz.notagardener.watering.Watering;

import java.util.Optional;

public interface WateringRepository extends Repository<Watering, Long>, WateringRepositoryCustom {
    Optional<Watering> findById(Long wateringId);
    Optional<Watering> findByWateringIdAndPlant_Gardener_GardenerId(Long wateringId, Long gardenerId);
    Watering save(Watering watering);
    void deleteById(Long wateringId);
    void delete(Watering watering);
    void deleteAllByPlant_PlantId(Long plantId);
}