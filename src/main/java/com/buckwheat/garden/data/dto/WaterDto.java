package com.buckwheat.garden.data.dto;

import com.buckwheat.garden.data.entity.Watering;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data // Getter, Setter, ToString, EqualsAndHashCode, RequiredArgsConstructor
@NoArgsConstructor
public class WaterDto {
    private int plantNo;
    private int fertilizerNo;
    private String fertilizerName;

    private LocalDate wateringDate;

    // DB -> client
    public WaterDto(Watering watering){
        this.plantNo = watering.getPlant().getNo();
        this.fertilizerNo = watering.getFertilizer().getFertilizerNo();
        this.fertilizerName = watering.getFertilizer().getFertilizerName();
        this.wateringDate = watering.getWateringDate();
    }

    // client -> DB
    public Watering toEntity(){
        return Watering
                .builder()
                // .fertilizer.(fertilizer)
                .wateringDate(LocalDate.now())
                .build();
    }
}
