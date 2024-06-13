package xyz.notagardener.repot.service;

import xyz.notagardener.plant.garden.dto.PlantResponse;
import xyz.notagardener.status.dto.PlantStatusResponse;

import java.time.LocalDate;
import java.util.List;

public interface RepotAlarmUtils {
    boolean isRepotNeeded(PlantResponse plantResponse);
    boolean isRepotNeeded(int recentWateringPeriod, int earlyWateringPeriod, List<PlantStatusResponse> status, LocalDate createDate);
}