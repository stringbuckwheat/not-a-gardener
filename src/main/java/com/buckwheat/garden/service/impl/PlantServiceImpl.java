package com.buckwheat.garden.service.impl;

import com.buckwheat.garden.data.dto.garden.GardenResponse;
import com.buckwheat.garden.data.dto.place.ModifyPlace;
import com.buckwheat.garden.data.dto.place.PlaceDto;
import com.buckwheat.garden.data.dto.plant.PlantRequest;
import com.buckwheat.garden.data.dto.plant.PlantResponse;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.projection.Calculate;
import com.buckwheat.garden.repository.command.PlantCommandRepository;
import com.buckwheat.garden.repository.query.PlantQueryRepository;
import com.buckwheat.garden.service.PlantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PlantServiceImpl implements PlantService {
    private final PlantCommandRepository plantCommandRepository;
    private final PlantQueryRepository plantQueryRepository;
    private final GardenResponseProvider gardenResponseProvider;

    @Override
    @Transactional(readOnly = true)
    public List<PlantResponse> getAll(Long gardenerId) {
        return plantQueryRepository.findByGardener_GardenerIdOrderByPlantIdDesc(gardenerId).stream()
                .map(PlantResponse::from)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public PlantResponse getDetail(Long plantId, Long gardenerId) {
        Plant plant = plantQueryRepository.findByPlantIdAndGardener_GardenerId(plantId, gardenerId)
                .orElseThrow(NoSuchElementException::new);

        Long totalWaterings = plantQueryRepository.countWateringByPlantId(plantId);
        LocalDate latestWateringDate = plantQueryRepository.findLatestWateringDateByPlantId(plantId);

        return PlantResponse.from(plant, totalWaterings, latestWateringDate);
    }

    @Override
    public GardenResponse add(Long gardenerId, PlantRequest plantRequest) {
        Plant plant = plantCommandRepository.save(gardenerId, plantRequest);
        return gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId));
    }

    @Override
    @Transactional
    public GardenResponse update(Long gardenerId, PlantRequest plantRequest) {
        Plant plant = plantCommandRepository.update(plantRequest, gardenerId);
        return gardenResponseProvider.getGardenResponse(Calculate.from(plant, gardenerId));
    }

    @Override
    public PlaceDto updatePlace(ModifyPlace modifyPlace, Long gardenerId) {
        return PlaceDto.from(plantCommandRepository.updatePlantPlace(modifyPlace, gardenerId));
    }

    @Override
    public void delete(Long plantId, Long gardenerId) {
        plantCommandRepository.deleteBy(plantId, gardenerId);
    }
}
