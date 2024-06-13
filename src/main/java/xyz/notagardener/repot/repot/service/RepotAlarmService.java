package xyz.notagardener.repot.repot.service;

import xyz.notagardener.common.validation.YesOrNoType;
import xyz.notagardener.plant.garden.dto.PlantResponse;

import java.time.LocalDate;

public interface RepotAlarmService {
    boolean isRepotNeeded(PlantResponse plantResponse);

    boolean isRepotNeeded(Long plantId, int recentWateringPeriod, int earlyWateringPeriod, YesOrNoType heavyDrinker, LocalDate createDate);
}
