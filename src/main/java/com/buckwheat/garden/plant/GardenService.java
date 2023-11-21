package com.buckwheat.garden.plant;

import com.buckwheat.garden.plant.garden.GardenMain;
import com.buckwheat.garden.plant.garden.GardenResponse;

import java.util.List;

public interface GardenService {
    GardenMain getGarden(Long gardenerId);

    List<GardenResponse> getAll(Long gardenerId);
}