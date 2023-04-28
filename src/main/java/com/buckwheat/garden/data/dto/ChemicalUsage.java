package com.buckwheat.garden.data.dto;

import java.time.LocalDate;

// TODO
public interface ChemicalUsage {
    Long getChemicalId();
    int getPeriod();
    String getName();
    LocalDate getLatestWateringDate();
}
