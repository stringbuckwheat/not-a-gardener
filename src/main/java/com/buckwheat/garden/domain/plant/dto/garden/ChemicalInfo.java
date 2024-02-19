package com.buckwheat.garden.domain.plant.dto.garden;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public class ChemicalInfo {
    @Schema(description = "약품 코드", example = "1")
    private Long chemicalId;

    @Schema(description = "약품 이름", example = "하이포넥스")
    private String chemicalName;
}
