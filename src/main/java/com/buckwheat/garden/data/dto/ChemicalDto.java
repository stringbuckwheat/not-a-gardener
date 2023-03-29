package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Chemical;
import com.buckwheat.garden.data.entity.Member;
import lombok.*;

public class ChemicalDto {
    @Getter
    @AllArgsConstructor
    @Builder
    @ToString
    public static class ChemicalResponse{
        private int chemicalNo;
        private String chemicalName;
        private String chemicalType;
        private int chemicalPeriod;

        public static ChemicalResponse from(Chemical chemical){
            return ChemicalResponse.builder()
                    .chemicalNo(chemical.getChemicalNo())
                    .chemicalName(chemical.getChemicalName())
                    .chemicalType(chemical.getChemicalType())
                    .chemicalPeriod(chemical.getChemicalPeriod())
                    .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @ToString
    public static class ChemicalRequest {
        private int chemicalNo;
        private String chemicalName;
        private String chemicalType;
        private int chemicalPeriod;

        /**
         * chemical update 시에 엔티티 생성(chemicalNo를 포함)
         * @param member
         * @return chemicalNo를 포함하는 chemical entity
         */
        public Chemical toEntityWithMemberForUpdate(Member member){
            return Chemical.builder()
                    .chemicalNo(chemicalNo)
                    .chemicalName(chemicalName)
                    .chemicalPeriod(chemicalPeriod)
                    .chemicalType(chemicalType)
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
                    .chemicalName(chemicalName)
                    .chemicalPeriod(chemicalPeriod)
                    .chemicalType(chemicalType)
                    .member(member)
                    .build();
        }
    }
}
