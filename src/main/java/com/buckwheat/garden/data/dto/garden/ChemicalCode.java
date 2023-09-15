package com.buckwheat.garden.data.dto.garden;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class ChemicalCode {
    private Long chemicalId;
    private String chemicalName;
}
