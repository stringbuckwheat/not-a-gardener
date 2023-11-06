package com.buckwheat.garden.service;

import com.buckwheat.garden.data.dto.garden.GardenMain;
import com.buckwheat.garden.data.dto.garden.GardenResponse;

import java.util.List;

public interface GardenService {
    GardenMain getGarden(Long gardenerId);
    List<GardenResponse> getAll(Long gardenerId);
}