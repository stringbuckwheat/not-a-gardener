package xyz.notagardener.plant.garden.service;

import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.RawGarden;

public interface GardenResponseMapper {
    GardenResponse getGardenResponse(RawGarden rawGarden, Long gardenerId);
}
