package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Watering;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data // Getter, Setter, ToString, EqualsAndHashCode, RequiredArgsConstructor
@NoArgsConstructor
public class WaterDto {
    private int plantNo;
    private String fertilized;

    private LocalDate wateringDate;

    // DB -> client
    public WaterDto(Watering watering){
        this.plantNo = watering.getPlantNo();
        this.fertilized = watering.getFertilized();
        this.wateringDate = watering.getWateringDate();
    }

    // client -> DB
    public Watering toEntity(){
        return Watering
                .builder()
                .fertilized(fertilized)
                .wateringDate(LocalDate.now())
                .build();
    }
}
