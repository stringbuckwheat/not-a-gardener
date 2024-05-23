package xyz.notagardener.plant.garden.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.plant.garden.dto.GardenMain;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.WaitingForWatering;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.routine.RoutineRepository;
import xyz.notagardener.routine.dto.RoutineResponse;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GardenServiceImpl implements GardenService {
    private final GardenResponseMapper gardenResponseMapper;
    private final PlantRepository plantRepository;
    private final RoutineRepository routineRepository;

    @Override
    @Transactional(readOnly = true)
    public GardenMain getGarden(Long gardenerId) {
        if (!plantRepository.existByGardenerId(gardenerId)) {
            return GardenMain.noPlant();
        }

        // 오늘 할 일
        List<GardenResponse> plantsToDo = plantRepository.findGardenByGardenerId(gardenerId).stream()
                .map(plantResponse -> gardenResponseMapper.getGardenResponse(plantResponse, gardenerId))
                .collect(Collectors.toList());

        // 물주기 기록을 기다리는 식물
        List<WaitingForWatering> waitingForWatering = plantRepository.findWaitingForWateringList(gardenerId);

        // 오늘 루틴 리스트
        List<RoutineResponse> routines = routineRepository.findByGardener_GardenerId(gardenerId).stream()
                .map(RoutineResponse::new)
                .filter(r -> !"N".equals(r.getHasToDoToday()))
                .collect(Collectors.toList());

        return new GardenMain(true, plantsToDo, waitingForWatering, routines);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GardenResponse> getAll(Long gardenerId) {
        return plantRepository.findAllPlantsWithLatestWateringDate(gardenerId).stream()
                .map(plantResponse -> gardenResponseMapper.getGardenResponse(plantResponse, gardenerId))
                .collect(Collectors.toList());
    }
}
