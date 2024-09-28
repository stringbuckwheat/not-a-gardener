package xyz.notagardener.watering.watering.service;

import xyz.notagardener.watering.model.Watering;
import xyz.notagardener.watering.watering.repository.WateringRepository;
import xyz.notagardener.watering.watering.dto.AfterWatering;
import xyz.notagardener.watering.watering.dto.WateringByDate;
import xyz.notagardener.watering.watering.dto.WateringRequest;
import xyz.notagardener.plant.model.Plant;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class WateringServiceImpl implements WateringService {
    private final WateringCommandService wateringCommandService;
    private final WateringRepository wateringRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<LocalDate, List<WateringByDate>> getAll(Long gardenerId, LocalDate date) {
        // 1일이 일요일이면 뒤로 2주 더
        // 1일이 일요일이 아니면 앞으로 한 주 뒤로 한 주
        LocalDate firstDayOfMonth = LocalDate.of(date.getYear(), date.getMonth(), 1);
        LocalDate startDate = getStartDate(firstDayOfMonth);
        LocalDate endDate = getEndDate(firstDayOfMonth);

        Map<LocalDate, List<WateringByDate>> result = new HashMap<>(); // 날짜: 리스트

        wateringRepository.findAllWateringListByGardenerId(gardenerId, startDate, endDate)
                .forEach(watering -> {
                    // key 값으로 들고오는데, 없으면 새 값을 맵에 추가해줌
                    result.computeIfAbsent(watering.getWateringDate(), key -> new ArrayList<>())
                            .add(WateringByDate.from(watering));
                });

        return result;
    }

    // 달력 시작 날짜
    private LocalDate getStartDate(LocalDate firstDayOfMonth) {
        // 월 화 수 목 금 토 일 => 1, 2, 3, 4, 5, 6, 7
        // 일요일이면 가만히 두고, 나머지 요일이면 getDayOfWeek().getValue()를 빼면
        // 7로 나눈 나머지 + 1을 뺌
        return firstDayOfMonth.minusDays(firstDayOfMonth.getDayOfWeek().getValue() % 7 + 1);
    }

    // 달력 마지막 날짜
    private LocalDate getEndDate(LocalDate firstDayOfMonth) {
        // end date: 합쳐서 42가 되게 만드는 값
        // x = 42 - 이번달 - startDate으로 더한 값
        int tmp = 42 - firstDayOfMonth.lengthOfMonth() - firstDayOfMonth.getDayOfWeek().getValue() % 7;
        return firstDayOfMonth.plusDays(firstDayOfMonth.lengthOfMonth() + tmp);
    }

    @Override
    public WateringByDate add(WateringRequest wateringRequest, Long gardenerId) {
        // 물주기 저장
        AfterWatering afterWatering = wateringCommandService.add(wateringRequest, gardenerId);

        Plant plant = afterWatering.getPlant();
        Watering latestWatering = plant.getWaterings().get(plant.getWaterings().size() - 1); // 방금 물 준 거 들고와야함

        return WateringByDate.from(latestWatering, plant, latestWatering.getChemical());
    }

    @Override
    public void delete(Long wateringId, Long plantId, Long gardenerId) {
        wateringCommandService.deleteById(wateringId, plantId, gardenerId);
    }
}
