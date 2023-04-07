package com.buckwheat.garden.data.dto;

import java.time.LocalDate;

public interface FertilizingInfo {
    int getChemicalNo();
    int getChemicalPeriod();
    String getChemicalName();
    LocalDate getLatestWateringDate();
}
