package com.buckwheat.garden.util;

import com.buckwheat.garden.data.dto.PlantDto;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import com.buckwheat.garden.repository.ChemicalRepository;
import com.buckwheat.garden.repository.PlantRepository;
import com.buckwheat.garden.repository.WateringRepository;
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
    private final ChemicalRepository chemicalRepository;
    private final WateringRepository wateringRepository;

    public WateringDto.WateringMsg getWateringMsg(int plantNo) {
        log.debug("getWateringMsg()");
        Plant plant = plantRepository.findByPlantNo(plantNo).orElseThrow(NoSuchElementException::new);
        log.debug("plant: {}", PlantDto.PlantResponse.from(plant));

        return getWateringMsg(plant);
    }

    public WateringDto.WateringMsg getWateringMsg(Plant plant){
        // 첫번째 물주기면
        if (plant.getWateringList().size() == 1) {
            return new WateringDto.WateringMsg(2, plant.getAverageWateringPeriod());
        }

        List<WateringDto.WateringForOnePlant> list = new ArrayList();
        for(Watering w: plant.getWateringList()){
            list.add(WateringDto.WateringForOnePlant.from(w));
        }

        // 이 메소드가 호출되는 시점엔 물주기 기록이 두 개 이상 있음
        LocalDateTime latestWateringDate = plant.getWateringList().get(0).getWateringDate().atStartOfDay();
        LocalDateTime prevWateringDate = plant.getWateringList().get(1).getWateringDate().atStartOfDay();

        int period = (int) Duration.between(prevWateringDate, latestWateringDate).toDays();
        int wateringCode = getWateringCode(period, plant.getAverageWateringPeriod());

        return new WateringDto.WateringMsg(wateringCode, period);
    }

    public int getWateringCode(int period, int prevWateringPeriod) {
        // 물주기 짧아짐: -1
        // 물주기 똑같음: 0
        // 물주기 길어짐: 1
        // 인간의 게으름 혹은 환경 문제이므로 DB 반영하지 않음
        return Integer.compare(period, prevWateringPeriod);
    }

    public Plant getPlantForAdd(Plant plant, int period) {
        if (period != plant.getAverageWateringPeriod()) {
            log.debug("average watering period 달라짐");
            Plant updatedPlant = plant.updateAverageWateringPeriod(period);
            plantRepository.save(updatedPlant);
            return updatedPlant;
        }

        return plant;
    }

    public WateringDto.WateringMsg addWatering(Plant plant, WateringDto.WateringRequest wateringRequest){
        // 맹물 줬는지 비료 타서 줬는지
        Chemical chemical = chemicalRepository.findById(wateringRequest.getChemicalNo()).orElse(null);

        // 저장
        Watering watering = wateringRepository.save(wateringRequest.toEntityWithPlantAndChemical(plant, chemical));

        return getWateringMsg(plant.getPlantNo());
    }
}
