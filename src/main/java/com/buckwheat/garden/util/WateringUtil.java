package com.buckwheat.garden.util;

import com.buckwheat.garden.code.AfterWateringCode;
import com.buckwheat.garden.data.dto.WateringDto;
import com.buckwheat.garden.data.entity.Plant;
import com.buckwheat.garden.data.entity.Watering;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class WateringUtil {

    public WateringDto.Message getWateringMsg(Plant plant){
        // 첫번째 물주기면
        if (plant.getWaterings().size() == 1) {
            return new WateringDto.Message(AfterWateringCode.FIRST_WATERING.getCode(), plant.getRecentWateringPeriod());
        } else if(plant.getWaterings().size() == 2){
            return new WateringDto.Message(AfterWateringCode.SECOND_WATERING.getCode(), plant.getRecentWateringPeriod());
        }

        LocalDateTime latestWateringDate = plant.getWaterings().get(0).getWateringDate().atStartOfDay();
        LocalDateTime prevWateringDate = plant.getWaterings().get(1).getWateringDate().atStartOfDay();

        int period = (int) Duration.between(prevWateringDate, latestWateringDate).toDays();

        if(plant.getWaterings().size() == 3){
            // 첫 물주기 측정 완료
            return new WateringDto.Message(AfterWateringCode.INIT_WATERING_PERIOD.getCode(), period);
        }

        // 물주기 짧아짐: -1
        // 물주기 똑같음: 0
        // 물주기 길어짐: 1
        int wateringCode = Integer.compare(period, plant.getRecentWateringPeriod());

        return new WateringDto.Message(wateringCode, period);
    }

    public List<WateringDto.ForOnePlant> withWateringPeriodList(List<Watering> list) {
        List<WateringDto.ForOnePlant> waterings = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            // 마지막 요소이자 가장 옛날 물주기
            // => 전 요소가 없으므로 며칠만에 물 줬는지 계산 X
            if (i == list.size() - 1) {
                waterings.add(WateringDto.ForOnePlant.from(list.get(i)));
                break;
            }

            // 며칠만에 물줬는지 계산
            LocalDateTime afterWateringDate = list.get(i).getWateringDate().atStartOfDay();
            LocalDateTime prevWateringDate = list.get(i + 1).getWateringDate().atStartOfDay();

            int wateringPeriod = (int) Duration.between(prevWateringDate, afterWateringDate).toDays();

            waterings.add(WateringDto.ForOnePlant.withWateringPeriodFrom(list.get(i), wateringPeriod));
        }

        return waterings;
    }
}
