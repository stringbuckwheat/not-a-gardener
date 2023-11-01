package com.buckwheat.garden.data.dto.chemical;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChemicalDetail {
    private ChemicalDto chemical;
    private int wateringSize;
}