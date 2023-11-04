package com.buckwheat.garden.dao;

import com.buckwheat.garden.data.dto.garden.RawGardenMain;
import com.buckwheat.garden.data.entity.Plant;

import java.util.List;

public interface GardenDao {
    Boolean hasAnyPlant(Long gardenerId);
    RawGardenMain findForGardenMain(Long gardenerId);
    List<Plant> findAllPlants(Long gardenerId);
}
