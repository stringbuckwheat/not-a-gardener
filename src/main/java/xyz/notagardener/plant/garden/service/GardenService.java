package xyz.notagardener.plant.garden.service;

import xyz.notagardener.plant.garden.dto.GardenMain;
import xyz.notagardener.plant.garden.dto.GardenResponse;

import java.util.List;

public interface GardenService {
    GardenMain getGarden(Long gardenerId);

    List<GardenResponse> getAll(Long gardenerId);
}