package xyz.notagardener.plant.garden.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
@ToString
public class ChemicalInfo {
    @Schema(description = "약품 id", example = "1")
    private Long chemicalId;

    @Schema(description = "약품 이름", example = "하이포넥스")
    private String chemicalName;
}
