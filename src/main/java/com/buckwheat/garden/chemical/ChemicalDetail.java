package com.buckwheat.garden.chemical;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChemicalDetail {
    private ChemicalDto chemical;
    private Long wateringSize;
}