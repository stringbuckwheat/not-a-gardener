package xyz.notagardener.domain.watering.repository;

import xyz.notagardener.domain.watering.Watering;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface WateringRepository extends Repository<Watering, Long>, WateringRepositoryCustom {
    Optional<Watering> findById(Long wateringId);

    Watering save(Watering watering);

    void deleteById(Long wateringId);

    @Transactional
    void deleteAllByPlant_PlantId(Long plantId);
}