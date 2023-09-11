package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Gardener;
import lombok.*;

import java.util.List;

public class ChemicalDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Basic {
        private Long id;
        private String name;
        private String type;
        private int period;

        public static Basic from(Chemical chemical) {
            return Basic.builder()
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

    @Getter
    @ToString
    @AllArgsConstructor
    public static class Detail {
        private Basic chemical;
        private List<WateringDto.ResponseInChemical> waterings;
    }
}
