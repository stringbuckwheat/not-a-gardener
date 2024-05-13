package xyz.notagardener.domain.chemical.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class ChemicalDetail {
    @Schema(description = "약품 정보")
    private ChemicalDto chemical;

    @Schema(description = "약품 시비 횟수", example = "24")
    private Long wateringSize;
}