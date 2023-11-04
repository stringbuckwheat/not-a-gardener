package com.buckwheat.garden.dao.impl;

import com.buckwheat.garden.dao.GardenDao;
import com.buckwheat.garden.data.dto.garden.RawGardenMain;
import com.buckwheat.garden.data.dto.garden.WaitingForWatering;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Routine;
import com.buckwheat.garden.data.projection.RawGarden;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.RoutineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class GardenDaoImpl implements GardenDao {
    private final PlantRepository plantRepository;
    private final RoutineRepository routineRepository;

    @Override
    public Boolean hasAnyPlant(Long gardenerId) {
        return plantRepository.existByGardenerId(gardenerId);
    }

    @Override
    public RawGardenMain findForGardenMain(Long gardenerId) {
        List<WaitingForWatering> waitingForWatering = plantRepository.findWaitingForWateringList(gardenerId);
        List<RawGarden> plantsToDo = plantRepository.findGardenByGardenerId(gardenerId);
        List<Routine> routines = routineRepository.findByGardener_GardenerId(gardenerId);
        return new RawGardenMain(waitingForWatering, plantsToDo, routines);
    }

    @Override
    public List<Plant> findAllPlants(Long gardenerId) {
        return plantRepository.findByGardener_GardenerId(gardenerId);
    }
}
