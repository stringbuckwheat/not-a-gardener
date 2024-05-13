package xyz.notagardener.domain.plant.service;

import xyz.notagardener.domain.plant.dto.garden.GardenMain;
import xyz.notagardener.domain.plant.dto.garden.GardenResponse;

import java.util.List;

public interface GardenService {
    GardenMain getGarden(Long gardenerId);

    List<GardenResponse> getAll(Long gardenerId);
}