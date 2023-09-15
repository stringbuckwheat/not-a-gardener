package com.buckwheat.garden.data.dto.chemical;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Gardener;
import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
@ToString
public class ChemicalDto {
    private Long id;
    private String name;
    private String type;
    private int period;

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

