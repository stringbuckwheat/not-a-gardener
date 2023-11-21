package com.buckwheat.garden.domain.watering.repository;

import com.buckwheat.garden.domain.plant.dto.projection.ChemicalUsage;
import com.buckwheat.garden.domain.watering.Watering;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface WateringRepository extends Repository<Watering, Long>, WateringRepositoryCustom {
    @Query(value = "SELECT chemical_id chemicalId, period, name," +
            " (SELECT " +
            " IFNULL(MAX(watering_date), "
            + "(SELECT MIN(watering_date) FROM watering w WHERE w.plant_id = :plantId))" +
            " FROM watering w " +
            " WHERE w.plant_id = :plantId AND w.chemical_id = c.chemical_id) latestWateringDate" +
            " FROM chemical c " +
            " WHERE c.gardener_id = :gardenerId AND c.active = :active" +
            " ORDER BY period DESC", nativeQuery = true)
    List<ChemicalUsage> findLatestChemicalizedDayList(@Param("gardenerId") Long gardenerId, @Param("plantId") Long plantId, @Param("active") String active);

    Optional<Watering> findById(Long wateringId);

    Watering save(Watering watering);

    void deleteById(Long wateringId);

    @Transactional
    void deleteAllByPlant_PlantId(Long plantId);
}