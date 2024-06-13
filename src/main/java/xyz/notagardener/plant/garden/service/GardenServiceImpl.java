package xyz.notagardener.plant.garden.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.garden.dto.AttentionRequiredPlant;
import xyz.notagardener.plant.garden.dto.GardenMain;
import xyz.notagardener.plant.garden.dto.GardenResponse;
import xyz.notagardener.plant.garden.dto.WaitingForWatering;
import xyz.notagardener.plant.plant.repository.PlantRepository;
import xyz.notagardener.routine.dto.RoutineResponse;
import xyz.notagardener.routine.repository.RoutineRepository;

import java.util.List;

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
        List<GardenResponse> todoList = plantRepository.findGardenByGardenerId(gardenerId).stream()
                .map(plantResponse -> gardenResponseMapper.getGardenResponse(plantResponse, gardenerId))
                .toList();

        // 물주기 기록을 기다리는 식물
        List<WaitingForWatering> waitingList = plantRepository.findWaitingForWateringList(gardenerId);

        // 오늘 루틴 리스트
        List<RoutineResponse> routineList = routineRepository.findByGardener_GardenerId(gardenerId).stream()
                .map(RoutineResponse::new)
                .filter(r -> !"N".equals(r.getHasToDoToday()))
                .toList();

        // 요주의 식물
        List<AttentionRequiredPlant> attentions = plantRepository.findByGardener_GardenerIdAndStatus_Attention(gardenerId, YesOrNoType.Y).stream()
                .map(AttentionRequiredPlant::new).toList();

        return GardenMain.builder()
                .hasPlant(true)
                .todoList(todoList)
                .waitingList(waitingList)
                .routineList(routineList)
                .attentions(attentions)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<GardenResponse> getAll(Long gardenerId) {
        return plantRepository.findAllPlantsWithLatestWateringDate(gardenerId).stream()
                .map(plantResponse -> gardenResponseMapper.getGardenResponse(plantResponse, gardenerId))
                .toList();
    }
}