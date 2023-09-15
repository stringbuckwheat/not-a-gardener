package com.buckwheat.garden.data.dto.chemical;

import com.buckwheat.garden.data.dto.watering.WateringResponseInChemical;
import lombok.*;

import java.util.List;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChemicalDetail {
    private ChemicalDto chemical;
    private List<WateringResponseInChemical> waterings;
}