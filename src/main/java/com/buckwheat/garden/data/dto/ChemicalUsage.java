package com.buckwheat.garden.data.dto;

import java.time.LocalDate;

// TODO
public interface ChemicalUsage {
    Long getChemicalId();
    int getChemicalPeriod();
    String getChemicalName();
    LocalDate getLatestWateringDate();
}
