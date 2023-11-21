package com.buckwheat.garden.domain.chemical.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChemicalDetail {
    private ChemicalDto chemical;
    private Long wateringSize;
}