package xyz.notagardener.repot.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import xyz.notagardener.status.PlantStatusType;

import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
public class RepotAlarmUtilsImpl implements RepotAlarmUtils {
    @Override
    public boolean isRepotNeeded(int recentWateringPeriod, int earlyWateringPeriod, String status, LocalDate createDate) {
        // 11월 ~ 3월이거나, 물 주기 간격 확보 안 됐거나, 최근 분갈이한 식물
        if (isTooColdToRepot() || isNotEnoughRecord(recentWateringPeriod, earlyWateringPeriod) || isRepottedRecently(status)) {
            return false;
        }

        return needsRepotting(recentWateringPeriod, earlyWateringPeriod, status, createDate);
    }

    private boolean isTooColdToRepot() {
        int thisMonth = LocalDate.now().getMonthValue();
        return thisMonth >= 11 || thisMonth <= 3;
    }

    private boolean isNotEnoughRecord(int recentWateringPeriod, int earlyWateringPeriod) {
        return recentWateringPeriod == 0 || earlyWateringPeriod == 0;
    }

    private boolean isRepottedRecently(String status) {
        return PlantStatusType.JUST_REPOTTED.getType().equals(status);
    }

    private boolean needsRepotting(int recentWateringPeriod, int earlyWateringPeriod, String status, LocalDate createDate) {
        if (isHeavyDrinker(status)) {
            return isHeavyDrinkerPastOneYear(createDate);
        }

        return isWateringPeriodReduced(earlyWateringPeriod, recentWateringPeriod);
    }

    private boolean isHeavyDrinker(String status) {
        return PlantStatusType.HEAVY_DRINKER.getType().equals(status);
    }

    private boolean isHeavyDrinkerPastOneYear(LocalDate createDate) {
        return createDate.plusYears(1).isBefore(LocalDate.now());
    }

    private boolean isWateringPeriodReduced(int earlyWateringPeriod, int recentWateringPeriod) {
        int floor = (int) Math.floor((double) earlyWateringPeriod / recentWateringPeriod);

        // 물주기 롱텀
        if (earlyWateringPeriod > 21) {
            return floor >= 3;
        } else {
            return floor >= 2;
        }
    }
}
