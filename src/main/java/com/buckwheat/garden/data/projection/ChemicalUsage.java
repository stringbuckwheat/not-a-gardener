package com.buckwheat.garden.data.projection;

import java.time.LocalDate;

public interface ChemicalUsage {
    Long getChemicalId();

    int getPeriod();

    String getName();

    LocalDate getLatestWateringDate();
}
