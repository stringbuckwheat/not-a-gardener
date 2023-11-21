package com.buckwheat.garden.plant;

import com.buckwheat.garden.code.AfterWateringCode;
import com.buckwheat.garden.watering.dto.WateringMessage;
import com.buckwheat.garden.data.entity.Watering;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public class PlantUtils {
    public static WateringMessage calculateWateringPeriod(List<Watering> waterings, int prevWateringPeriod){
        // 첫번째 물주기면
        if (waterings.size() == 0) {
            return null;
        } else if (waterings.size() == 1) {
            return new WateringMessage(AfterWateringCode.FIRST_WATERING.getCode(), prevWateringPeriod);
        } else if (waterings.size() == 2) {
            return new WateringMessage(AfterWateringCode.SECOND_WATERING.getCode(), prevWateringPeriod);
        }

        LocalDateTime latestWateringDate = waterings.get(0).getWateringDate().atStartOfDay();
        LocalDateTime prevWateringDate = waterings.get(1).getWateringDate().atStartOfDay();

        int period = (int) Duration.between(prevWateringDate, latestWateringDate).toDays();

        if (waterings.size() == 3) {
            return new WateringMessage(AfterWateringCode.INIT_WATERING_PERIOD.getCode(), period);
        }

        // 물주기 짧아짐: -1
        // 물주기 똑같음: 0
        // 물주기 길어짐: 1
        int afterWateringCode = Integer.compare(period, prevWateringPeriod);

        return new WateringMessage(afterWateringCode, period);
    }
}
