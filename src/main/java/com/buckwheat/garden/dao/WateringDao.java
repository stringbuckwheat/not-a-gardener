package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.ChemicalUsage;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Watering;

import java.time.LocalDate;
import java.util.List;

public interface WateringDao {
    Watering addWatering(WateringDto.Request wateringRequest);
    List<Watering> getWateringListByPlantNo(Long plantId);
    List<Watering> getAllWateringListByGardenerNo(Long gardenerId, LocalDate startDate, LocalDate endDate);
    List<ChemicalUsage> getLatestChemicalUsages(Long gardenerId, Long plantId);
    Watering modifyWatering(WateringDto.Request wateringRequest);
    void deleteById(Long wateringId);
    void deleteByPlantId(Long plantId);
}
