package xyz.notagardener.plant.garden.service;

import xyz.notagardener.watering.watering.dto.ChemicalUsage;

import java.util.List;

public interface ChemicalInfoService {
    List<ChemicalUsage> getChemicalUsagesByPlantId(Long plantId, Long gardenerId);
}
