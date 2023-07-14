package com.buckwheat.garden.data.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RawGarden {
    Long getPlantId();
    String getName();
    String getSpecies();
    int getRecentWateringPeriod();
    int getEarlyWateringPeriod();
    String getMedium();
    LocalDate getBirthday();
    LocalDate getConditionDate();
    LocalDate getPostponeDate();
    LocalDateTime getCreateDate();

    Long getPlaceId();
    String getPlaceName();

    Long getWateringId();
    LocalDate getLatestWateringDate();
}
