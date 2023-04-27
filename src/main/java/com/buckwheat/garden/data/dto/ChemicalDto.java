package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Member;
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
         * @param member
         * @return chemicalNo를 포함하는 chemical entity
         */
        public Chemical toEntityWithMemberForUpdate(Member member){
            return Chemical.builder()
                    .chemicalId(id)
                    .name(name)
                    .period(period)
                    .type(type)
                    .member(member)
                    .build();
        }

        /**
         * chemical insert 시의 엔티티 생성(chemicalNo 포함 X)
         * @param member
         * @return chemicalNo가 없는 chemical entity
         */
        public Chemical toEntityWithMember(Member member){
            return Chemical.builder()
                    .name(name)
                    .period(period)
                    .type(type)
                    .member(member)
                    .build();
        }
    }
}
