package com.buckwheat.garden.data.projection;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
@NoArgsConstructor
public class ChemicalUsageDto {
    private long chemicalId;
    private int period;
    private String name;
    private LocalDate latestWateringDate;

    @QueryProjection
    public ChemicalUsageDto(long chemicalId, int period, String name, LocalDate latestWateringDate) {
        this.chemicalId = chemicalId;
        this.period = period;
        this.name = name;
        this.latestWateringDate = latestWateringDate;
    }
}
