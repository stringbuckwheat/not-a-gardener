package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Gardener;
import lombok.*;

public class ChemicalDto {
    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class Response{
        private Long id;
        private String name;
        private String type;
        private int period;

        public static Response from(Chemical chemical){
            return Response.builder()
                    .id(chemical.getChemicalId())
                    .name(chemical.getName())
                    .type(chemical.getType())
                    .period(chemical.getPeriod())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Request {
        private Long id;
        private String name;
        private String type;
        private int period;

        /**
         * chemical update 시에 엔티티 생성(chemicalNo를 포함)
         * @param gardener
         * @return chemicalNo를 포함하는 chemical entity
         */
        public Chemical toEntityWithGardenerForUpdate(Gardener gardener){
            return Chemical.builder()
                    .chemicalId(id)
                    .name(name)
                    .period(period)
                    .type(type)
                    .gardener(gardener)
                    .build();
        }

        /**
         * chemical insert 시의 엔티티 생성(chemicalNo 포함 X)
         * @param gardener
         * @return chemicalNo가 없는 chemical entity
         */
        public Chemical toEntityWithGardener(Gardener gardener){
            return Chemical.builder()
                    .name(name)
                    .period(period)
                    .type(type)
                    .gardener(gardener)
                    .build();
        }
    }
}
