package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Gardener;
import lombok.*;

import java.util.List;

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
    @ToString
    @AllArgsConstructor
    public static class Detail {
        private Response chemical;
        private List<WateringDto.ResponseInChemical> waterings;
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class Request {
        private Long id;
        private String name;
        private String type;
        private int period;
        private String active;

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
                    .active(active)
                    .build();
        }
    }
}
