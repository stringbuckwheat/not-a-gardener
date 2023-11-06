package com.buckwheat.garden.repository.query;

import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.data.projection.ChemicalUsage;
import com.buckwheat.garden.repository.query.querydsl.WateringRepositoryCustom;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WateringQueryRepository extends Repository<Watering, Long>, WateringRepositoryCustom {
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
