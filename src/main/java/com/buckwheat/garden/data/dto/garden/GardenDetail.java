package com.buckwheat.garden.data.dto.garden;

import com.buckwheat.garden.code.WateringCode;
import com.buckwheat.garden.data.dto.watering.WateringResponse;
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
    // 마지막 관수
    private WateringResponse latestWateringDate;

    // 이하 계산해서 넣는 정보
    private String anniversary; // 키운지 며칠 지났는지
    private int wateringDDay;

    // 물주기 정보
    private int wateringCode;

    // 비료 주기 정보
    ChemicalCode chemicalCode;

    public static GardenDetail lazy(LocalDate latestWateringDate, LocalDate birthday) {
        return GardenDetail.builder()
                .latestWateringDate(latestWateringDate == null ?
                        null : WateringResponse.from(latestWateringDate))
                .anniversary(getAnniversary(birthday))
                .wateringDDay(latestWateringDate == null ? -1 : 0)
                .wateringCode(WateringCode.YOU_ARE_LAZY.getCode())
                .chemicalCode(null)
                .build();
    }

    public static GardenDetail noRecord(LocalDate birthday) {
        return GardenDetail.builder()
                .latestWateringDate(null)
                .anniversary(getAnniversary(birthday))
                .wateringDDay(-1)
                .wateringCode(WateringCode.NO_RECORD.getCode())
                .chemicalCode(null)
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
