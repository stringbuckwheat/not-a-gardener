package xyz.notagardener.chemical;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ChemicalType {
    NPK_FERTILIZER("기본 NPK 비료"),
    BLOOM_FERTILIZER("개화용 비료"),
    MICRONUTRIENT_FERTILIZER("미량 원소 비료"),
    INSECTICIDE_PESTICIDE("살충제/농약"),
    FUNGICIDE_PESTICIDE("살균제/농약"),
    OTHER("그 외");

    public final String type;

    // 위 enum 중에 존재하는지 검사
    public static boolean isValid(String type) {
        for(ChemicalType chemicalType : values()) {
            if(chemicalType.getType().equals(type)) {
                return true;
            }
        }

        return false;
    }
}
