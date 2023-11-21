package com.buckwheat.garden.domain.plant.dto.projection;

import java.time.LocalDate;

public interface ChemicalUsage {
    Long getChemicalId();

    int getPeriod();

    String getName();

    LocalDate getLatestWateringDate();
}
