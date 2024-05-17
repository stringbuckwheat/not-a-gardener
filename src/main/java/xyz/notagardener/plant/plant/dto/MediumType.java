package xyz.notagardener.plant.plant.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MediumType {
    BASIC_POT("흙과 화분"),
    SPHAGNUM_MOSS("수태"),
    SEMI_HYDROPONIC("반수경"),
    HYDROPONIC("수경"),
    TERRARIUM("테라리움");

    public final String type;

    public static boolean isValid(String type) {
        for(MediumType mediumType : values()) {
            if(mediumType.getType().equals(type)) {
                return true;
            }
        }

        return false;
    }
}
