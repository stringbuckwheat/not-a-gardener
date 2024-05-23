package xyz.notagardener.plant.garden.service;

import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.PlantResponse;

public interface GardenResponseMapper {
    GardenResponse getGardenResponse(PlantResponse plantResponse, Long gardenerId);
}
