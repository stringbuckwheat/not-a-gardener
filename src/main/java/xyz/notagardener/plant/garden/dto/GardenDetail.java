package xyz.notagardener.plant.garden.dto;

import xyz.notagardener.watering.code.WateringCode;
import xyz.notagardener.watering.dto.WateringResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.time.Duration;
import java.time.LocalDate;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class GardenDetail {
    @Schema(description = "가장 최근 물 준 날짜", example = "2023-01-28")
    private WateringResponse latestWateringDate;

    @Schema(description = "키운지 며칠 지났는지", example = "999")
    private String anniversary;

    @Schema(description = "물 준지 며칠 지났는지", example = "2")
    private int wateringDDay;

    @Schema(description = "물 주기 코드", example = "2")
    private int wateringCode;

    @Schema(description = "(오늘 비료를 줄 날이라면) 비료 정보")
    ChemicalInfo chemicalInfo;

    public static GardenDetail lazy(LocalDate latestWateringDate, LocalDate birthday) {
        return GardenDetail.builder()
                .latestWateringDate(latestWateringDate == null ?
                        null : WateringResponse.from(latestWateringDate))
                .anniversary(getAnniversary(birthday))
                .wateringDDay(latestWateringDate == null ? -1 : 0)
                .wateringCode(WateringCode.YOU_ARE_LAZY.getCode())
                .chemicalInfo(null)
                .build();
    }

    public static GardenDetail noRecord(LocalDate birthday) {
        return GardenDetail.builder()
                .latestWateringDate(null)
                .anniversary(getAnniversary(birthday))
                .wateringDDay(-1)
                .wateringCode(WateringCode.NOT_ENOUGH_RECORD.getCode())
                .chemicalInfo(null)
                .build();
    }

    public static String getAnniversary(LocalDate birthday) {
        if (birthday == null) {
            return "";
        }

        LocalDate today = LocalDate.now();

        // 생일이면
        if (today.getMonth() == birthday.getMonth() && today.getDayOfMonth() == birthday.getDayOfMonth()) {
            return "생일 축하해요";
        }

        return Duration.between(birthday.atStartOfDay(), today.atStartOfDay()).toDays() + "일 째 반려중";
    }
}
