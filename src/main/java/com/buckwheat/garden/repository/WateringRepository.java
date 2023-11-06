package com.buckwheat.garden.repository;

import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.data.projection.ChemicalUsage;
import com.buckwheat.garden.repository.querydsl.WateringRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface WateringRepository extends JpaRepository<Watering, Long>, WateringRepositoryCustom {
    int countByChemical_ChemicalId(Long chemicalId);
    int countByPlant_PlantId(Long placeId);

    @Transactional
    void deleteAllByPlant_PlantId(Long plantId);

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
}