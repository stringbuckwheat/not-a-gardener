package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.dao.WateringDao;
import com.buckwheat.garden.data.dto.ChemicalUsage;
import com.buckwheat.garden.data.dto.GardenDto;
import com.buckwheat.garden.data.dto.PlantDto;
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

    public GardenDto.Response getGardenResponse(Plant plant, Long memberId) {
        if (plant.getPostponeDate() != null && LocalDate.now().compareTo(plant.getPostponeDate()) == 0) {
            return getGardenResponseWhenLazy(plant);
        }

        PlantDto.Response plantResponse = PlantDto.Response.from(plant);

        List<ChemicalUsage> chemicalUsages = wateringDao.getLatestChemicalUsages(plant.getPlantId(), memberId);
        GardenDto.Detail gardenDetail = gardenUtil.getGardenDetail(plant, chemicalUsages);

        return new GardenDto.Response(plantResponse, gardenDetail);
    }

    public GardenDto.Response getGardenResponseWhenLazy(Plant plant){
        PlantDto.Response plantResponse = PlantDto.Response.from(plant);
        GardenDto.Detail gardenDetail = gardenUtil.getGardenDetailWhenLazy(plant);
        return new GardenDto.Response(plantResponse, gardenDetail);
    }

}
