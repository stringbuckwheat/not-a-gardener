package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.watering.AfterWatering;
import com.buckwheat.garden.data.dto.watering.WateringMessage;
import com.buckwheat.garden.data.dto.watering.WateringRequest;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.data.projection.ChemicalUsage;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface WateringDao {
    AfterWatering addWatering(WateringRequest wateringRequest);
    WateringMessage updateWateringPeriod(Plant plant);
    List<Watering> getWateringListByPlantId(Long plantId, Pageable pageable);
    List<Watering> getAllWateringListByGardenerId(Long gardenerId, LocalDate startDate, LocalDate endDate);
    List<ChemicalUsage> getLatestChemicalUsages(Long gardenerId, Long plantId);
    List<Watering> getWateringsByChemicalIdWithPage(Long chemicalId, Pageable pageable);
    int getCountByChemical_ChemicalId(Long chemicalId);
    AfterWatering modifyWatering(WateringRequest wateringRequest, Long gardenerId);
    WateringMessage deleteById(Long wateringId, Long plantId, Long gardenerId);
    void deleteByPlantId(Long plantId);
}
