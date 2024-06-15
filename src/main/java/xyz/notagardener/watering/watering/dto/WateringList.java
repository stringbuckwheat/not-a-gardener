package xyz.notagardener.watering.watering.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@Getter
@ToString
public class WateringList {
    @Schema(description = "물 준 날짜", example = "2024-06-16")
    private LocalDate wateringDate;
    List<WateringByDate> waterings;
}
