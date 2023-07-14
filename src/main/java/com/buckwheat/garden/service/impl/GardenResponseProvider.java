package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.ChemicalUsage;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.RawGarden;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.util.GardenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GardenResponseProvider {
    private final WateringDao wateringDao;
    private final GardenUtil gardenUtil;

    public GardenDto.Response getGardenResponse(RawGarden rawGarden, Long gardenerId) {
        if (rawGarden.getPostponeDate() != null && LocalDate.now().compareTo(rawGarden.getPostponeDate()) == 0) {
            return getGardenResponseWhenLazy(rawGarden);
        }

        PlantDto.Response plantResponse = PlantDto.Response.from(rawGarden);

        List<ChemicalUsage> chemicalUsages = wateringDao.getLatestChemicalUsages(rawGarden.getPlantId(), gardenerId);
        GardenDto.Detail gardenDetail = gardenUtil.getGardenDetail(rawGarden, chemicalUsages);

        return new GardenDto.Response(plantResponse, gardenDetail);
    }

    public GardenDto.Response getGardenResponse(Plant plant, Long gardenerId) {
        if (plant.getPostponeDate() != null && LocalDate.now().compareTo(plant.getPostponeDate()) == 0) {
            return getGardenResponseWhenLazy(plant);
        }

        PlantDto.Response plantResponse = PlantDto.Response.from(plant);

        List<ChemicalUsage> chemicalUsages = wateringDao.getLatestChemicalUsages(plant.getPlantId(), gardenerId);
        GardenDto.Detail gardenDetail = gardenUtil.getGardenDetail(plant, chemicalUsages);

        return new GardenDto.Response(plantResponse, gardenDetail);
    }

    public GardenDto.Response getGardenResponseWhenLazy(Plant plant){
        PlantDto.Response plantResponse = PlantDto.Response.from(plant);
        GardenDto.Detail gardenDetail = gardenUtil.getGardenDetailWhenLazy(plant);
        return new GardenDto.Response(plantResponse, gardenDetail);
    }

    public GardenDto.Response getGardenResponseWhenLazy(RawGarden rawGarden){
        PlantDto.Response plantResponse = PlantDto.Response.from(rawGarden);
        GardenDto.Detail gardenDetail = gardenUtil.getGardenDetailWhenLazy(rawGarden);
        return new GardenDto.Response(plantResponse, gardenDetail);
    }
}
