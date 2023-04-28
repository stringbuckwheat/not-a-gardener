package com.buckwheat.garden.util;

import com.buckwheat.garden.code.AfterWateringCode;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.PlantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@Component
@RequiredArgsConstructor
public class WateringUtil {
    private final PlantRepository plantRepository;

    public WateringDto.Message getWateringMsg(Long plantId) {
        Plant plant = plantRepository.findByPlantId(plantId).orElseThrow(NoSuchElementException::new);
        return getWateringMsg(plant);
    }

    public WateringDto.Message getWateringMsg(Plant plant){
        // 첫번째 물주기면
        if (plant.getWaterings().size() == 1) {
            return new WateringDto.Message(AfterWateringCode.FIRST_WATERING.getCode(), plant.getRecentWateringPeriod());
        }

        List<WateringDto.ForOnePlant> list = new ArrayList();
        for(Watering w: plant.getWaterings()){
            list.add(WateringDto.ForOnePlant.from(w));
        }

        // 이 메소드가 호출되는 시점엔 물주기 기록이 두 개 이상 있음
        LocalDateTime latestWateringDate = plant.getWaterings().get(0).getWateringDate().atStartOfDay();
        LocalDateTime prevWateringDate = plant.getWaterings().get(1).getWateringDate().atStartOfDay();

        int period = (int) Duration.between(prevWateringDate, latestWateringDate).toDays();
        int wateringCode = getAfterWateringCode(period, plant.getRecentWateringPeriod());

        return new WateringDto.Message(wateringCode, period);
    }

    public int getAfterWateringCode(int period, int prevWateringPeriod) {
        // 물주기 짧아짐: -1
        // 물주기 똑같음: 0
        // 물주기 길어짐: 1
        // 인간의 게으름 혹은 환경 문제이므로 DB 반영하지 않음
        return Integer.compare(period, prevWateringPeriod);
    }

    public Plant updateWateringPeriod(Plant plant, int period) {
        if (period != plant.getRecentWateringPeriod()) {
            log.debug("average watering period 변동");
            Plant updatedPlant = plant.updateAverageWateringPeriod(period);
            plantRepository.save(updatedPlant);
            return updatedPlant;
        }

        return plant;
    }
}
