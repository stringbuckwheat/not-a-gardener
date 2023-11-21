package com.buckwheat.garden.domain.chemical.dto;

import com.buckwheat.garden.domain.chemical.Chemical;
import com.buckwheat.garden.domain.gardener.Gardener;
import com.querydsl.core.annotations.QueryProjection;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@ToString
public class ChemicalDto {
    private Long id;
    private String name;
    private String type;
    private int period;

    @QueryProjection
    public ChemicalDto(Long id, String name, String type, int period) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.period = period;
    }

    public static ChemicalDto from(Chemical chemical) {
        return ChemicalDto.builder()
                .id(chemical.getChemicalId())
                .name(chemical.getName())
                .type(chemical.getType())
                .period(chemical.getPeriod())
                .build();
    }

    public Chemical toEntity(Gardener gardener) {
        return Chemical.builder()
                .name(name)
                .period(period)
                .type(type)
                .gardener(gardener)
                .active("Y")
                .build();
    }

    public Chemical toEntityForUpdate(Gardener gardener) {
        return Chemical.builder()
                .chemicalId(id)
                .name(name)
                .period(period)
                .type(type)
                .gardener(gardener)
                .active("Y")
                .build();
    }
}

