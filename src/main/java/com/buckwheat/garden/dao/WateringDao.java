package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.data.projection.ChemicalUsage;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface WateringDao {
    Watering addWatering(WateringRequest wateringRequest);
    List<Watering> getWateringListByPlantId(Long plantId);
    List<Watering> getAllWateringListByGardenerId(Long gardenerId, LocalDate startDate, LocalDate endDate);
    List<ChemicalUsage> getLatestChemicalUsages(Long gardenerId, Long plantId);
    List<Watering> getWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable);
    Long getCountByChemical_ChemicalId(Long chemicalId);
    Watering modifyWatering(WateringRequest wateringRequest);
    void deleteById(Long wateringId);
    void deleteByPlantId(Long plantId);
}
