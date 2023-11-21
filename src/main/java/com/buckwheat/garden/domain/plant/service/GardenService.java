package com.buckwheat.garden.domain.plant.service;

import com.buckwheat.garden.domain.plant.dto.garden.GardenMain;
import com.buckwheat.garden.domain.plant.dto.garden.GardenResponse;

import java.util.List;

public interface GardenService {
    GardenMain getGarden(Long gardenerId);

    List<GardenResponse> getAll(Long gardenerId);
}