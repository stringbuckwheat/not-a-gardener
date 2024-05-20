package xyz.notagardener.watering.watering.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@ToString
public class ChemicalUsage {
    private Long chemicalId;
    private int period;
    private String name;
    private LocalDate latestWateringDate;

    @QueryProjection
    public ChemicalUsage(Long chemicalId, int period, String name, LocalDate latestWateringDate) {
        this.chemicalId = chemicalId;
        this.period = period;
        this.name = name;
        this.latestWateringDate = latestWateringDate;
    }
}
